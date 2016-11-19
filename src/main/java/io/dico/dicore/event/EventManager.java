package io.dico.dicore.event;

import io.dico.dicore.util.Reflection;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class EventManager {

    private static final EventHandler DEFAULT;
    private static final Listener EMPTY;

    public static <T extends Event> void registerListener(Plugin plugin, Class<T> clazz, EventListener<T> handler) {
        Method executor = Reflection.getMethod(handler, "execute");
        if (executor == null) {
            return;
        }

        EventHandler annot = executor.getAnnotation(EventHandler.class);
        if (annot == null) {
            annot = DEFAULT;
        }
        EventPriority priority = annot.priority();
        boolean ignoreCancelled = annot.ignoreCancelled();

        RegisteredListener listener = new RegisteredListener(plugin, )

        registerListener(clazz, EventPriority.NORMAL, handler);
    }

    private static RegisteredListener createRegistration() {
        return new RegisteredListener()
    }

    static {
        DEFAULT = new EventHandler() {
            @Override
            public EventPriority priority() {
                return EventPriority.NORMAL;
            }

            @Override
            public boolean ignoreCancelled() {
                return false;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return EventHandler.class;
            }
        };

        EMPTY = new Listener() {
        };
    }

    private static <T extends Event> EventExecutor fromEventListener(EventListener<T> listener, Class<T> clazz) {
        return new EventExecutor() {
            @Override
            public void execute(Listener ignored, Event event) throws EventException {

                try {
                    listener.execute(clazz.cast(event));
                } catch (ClassCastException e) {
                    throw new EventException("Listener called with wrong event type");
                } catch (EventException e) {

                }




            }
        };
    }

}
