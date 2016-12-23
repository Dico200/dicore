package io.dico.dicore.util;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.bukkit.event.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
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

    public Registrator(Plugin plugin) {
        this.plugin = plugin;
        if (plugin.isEnabled()) {
            setEnabled(true);
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() == plugin) {
            setEnabled(true);
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
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
            myListeners.add(new Listener() {
            });
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

        RegisteredListener listener = new RegisteredListener(getListenerFor(handlerList, priority), (ignoredListener, event) -> {
            try {
                T eventCasted = (T) event;
                handler.accept(eventCasted);
            } catch (ClassCastException ignored) {
            }
        }, priority, plugin, ignoreCancelled);

        listeners.put(handlerList, listener);
        if (enabled) {
            handlerList.register(listener);
        }
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

    public void unregisterAll() {
        for (Map.Entry<HandlerList, Collection<RegisteredListener>> entry : listeners.asMap().entrySet()) {
            for (RegisteredListener listener : entry.getValue()) {
                entry.getKey().unregister(listener);
            }
        }
    }

    public void reregisterAll() {
        for (Map.Entry<HandlerList, Collection<RegisteredListener>> entry : listeners.asMap().entrySet()) {
            for (RegisteredListener listener : entry.getValue()) {
                entry.getKey().register(listener);
            }
        }
    }

}
