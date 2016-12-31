package io.dico.dicore.util;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class Registrator implements Listener {

    private Plugin plugin;
    private ListMultimap<HandlerList, RegisteredListener> listeners = LinkedListMultimap.create();
    private List<Listener> myListeners = new ArrayList<>(Collections.singletonList(this));
    private boolean enabled = false;
    private RegisteredListener pluginEnableListener, pluginDisableListener;

    public Registrator(Plugin plugin) {
        Listener pluginStateChangeListener = newEmptyListener();
        pluginEnableListener = createRegistration(pluginStateChangeListener, EventPriority.NORMAL, false, PluginEnableEvent.class, this::onPluginEnable);
        pluginDisableListener = createRegistration(pluginStateChangeListener, EventPriority.NORMAL, false, PluginDisableEvent.class, this::onPluginDisable);
        setPlugin(plugin);
    }

    public void setPlugin(Plugin plugin) {
        Objects.requireNonNull(plugin);
        if (this.plugin == plugin) {
            return;
        }

        if (this.plugin != null) {
            setEnabled(false);
            unregisterEnableListeners();
        }

        this.plugin = plugin;
        registerEnableListeners();

        if (plugin.isEnabled()) {
            setEnabled(true);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void disable() {
        unregisterEnableListeners();
        setEnabled(false);
    }

    public void enable() {
        registerEnableListeners();
        setEnabled(true);
    }

    private void registerEnableListeners() {
        PluginEnableEvent.getHandlerList().register(pluginEnableListener = setPluginCorrectly(pluginEnableListener));
        PluginDisableEvent.getHandlerList().register(pluginDisableListener = setPluginCorrectly(pluginDisableListener));
    }

    private void unregisterEnableListeners() {
        PluginEnableEvent.getHandlerList().unregister(pluginEnableListener);
        PluginDisableEvent.getHandlerList().unregister(pluginDisableListener);
    }

    private void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() == plugin) {
            setEnabled(true);
        }
    }

    private void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() == plugin) {
            setEnabled(false);
        }
    }

    private void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                reregisterAll();
            } else {
                unregisterAll();
            }
        }
    }

    private Listener getListenerFor(HandlerList list, EventPriority priority) {
        int needed = (int) (listeners.get(list).stream().filter(listener -> listener.getPriority() == priority).count() + 1);
        while (needed > myListeners.size()) {
            myListeners.add(newEmptyListener());
        }
        return myListeners.get(needed - 1);
    }

    public ListMultimap<HandlerList, RegisteredListener> getListeners() {
        return Multimaps.unmodifiableListMultimap(listeners);
    }

    public <T extends Event> void registerListener(Class<T> eventClass, Consumer<T> handler) {
        registerListener(eventClass, EventPriority.HIGHEST, handler);
    }

    public <T extends Event> void registerListener(Class<T> eventClass, EventPriority priority, Consumer<T> handler) {
        boolean ignoreCancelled;
        switch (priority) {
            case LOWEST:
            case LOW:
                ignoreCancelled = false;
                break;
            default:
                ignoreCancelled = true;
        }
        registerListener(eventClass, priority, ignoreCancelled, handler);
    }

    public <T extends Event> void registerListener(Class<T> eventClass, boolean ignoreCancelled, Consumer<T> handler) {
        registerListener(eventClass, ignoreCancelled ? EventPriority.HIGHEST : EventPriority.LOW, ignoreCancelled, handler);
    }

    public <T extends Event> void registerListener(Class<T> eventClass, EventPriority priority, boolean ignoreCancelled, Consumer<T> handler) {
        HandlerList handlerList = getHandlerList(eventClass);
        RegisteredListener listener = createRegistration(getListenerFor(handlerList, priority), priority, ignoreCancelled, eventClass, handler);
        register(handlerList, listener);
    }

    private void register(HandlerList handlerList, RegisteredListener listener) {
        listeners.put(handlerList, listener);
        if (enabled) {
            handlerList.register(listener);
        }
    }

    private RegisteredListener setPluginCorrectly(RegisteredListener listener) {
        if (listener.getPlugin() != plugin) {
            EventExecutor executor = null;
            try {
                executor = (EventExecutor) Reflection.getValueInField(RegisteredListener.class, "executor", listener);
            } catch (ClassCastException ignored) {
            }
            if (executor != null) {
                listener = new RegisteredListener(listener.getListener(), executor, listener.getPriority(), plugin, listener.isIgnoringCancelled());
            } else {
                throw new IllegalArgumentException("Invalid plugin");
            }
        }
        return listener;
    }

    private <T extends Event> RegisteredListener createRegistration(Listener listener, EventPriority priority, boolean ignoreCancelled, Class<T> eventClass, Consumer<T> handler) {
        EventExecutor executor = newEventExecutor(eventClass, handler);
        return new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled);
    }

    private void unregisterAll() {
        for (Map.Entry<HandlerList, Collection<RegisteredListener>> entry : listeners.asMap().entrySet()) {
            HandlerList handlerList = entry.getKey();
            Collection<RegisteredListener> value = entry.getValue();
            value.forEach(handlerList::unregister);
        }
    }

    private void setPluginCorrectly(Collection<RegisteredListener> collection) {
        Collection<RegisteredListener> toAdd = null;
        Iterator<RegisteredListener> iterator = collection.iterator();
        while (iterator.hasNext()) {
            RegisteredListener next = iterator.next();
            RegisteredListener replacement;
            try {
                replacement = setPluginCorrectly(next);
            } catch (Exception e) {
                continue;
            }

            if (replacement != next) {
                iterator.remove();
                if (toAdd == null) {
                    toAdd = new LinkedList<>();
                }
                toAdd.add(replacement);
            }
        }

        if (toAdd != null) {
            collection.addAll(toAdd);
        }
    }

    private void reregisterAll() {
        for (Map.Entry<HandlerList, Collection<RegisteredListener>> entry : listeners.asMap().entrySet()) {
            HandlerList handlerList = entry.getKey();
            Collection<RegisteredListener> value = entry.getValue();
            setPluginCorrectly(value);
            value.forEach(handlerList::register);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Event> EventExecutor newEventExecutor(Class<T> eventClass, Consumer<T> handler) {
        if (eventClass == Event.class) {
            return (ignored, event) -> handler.accept((T) event);
        }
        return (ignored, event) -> {
            T eventCasted;
            try {
                eventCasted = (T) event;
            } catch (ClassCastException e) {
                return;
            }
            handler.accept(eventCasted);
        };
    }

    private static HandlerList getHandlerList(Class<?> eventClass) {
        Method m = null;
        Class<?> checkedClass = eventClass;
        while (checkedClass != Event.class && Event.class.isAssignableFrom(checkedClass)) {
            try {
                m = checkedClass.getDeclaredMethod("getHandlerList");
                break;
            } catch (NoSuchMethodException ignored) {
            }
            checkedClass = checkedClass.getSuperclass();
        }

        try {
            if (m == null) {
                throw new NullPointerException();
            }
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            return (HandlerList) m.invoke(null);
        } catch (InvocationTargetException | IllegalAccessException | NullPointerException | ClassCastException e) {
            throw new IllegalArgumentException("Failed to get HandlerList of " + eventClass.getCanonicalName(), e);
        }
    }

    private static Listener newEmptyListener() {
        return new Listener() {
        };
    }

    // cool stuff

    public void onPlayerQuit(Consumer<Player> consumer) {
        registerListener(PlayerQuitEvent.class, event -> consumer.accept(event.getPlayer()));
        registerListener(PlayerKickEvent.class, event -> consumer.accept(event.getPlayer()));
    }

}
