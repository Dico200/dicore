package io.dico.dicore.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;

public interface EventListener<T extends Event> {

    boolean execute(T event) throws EventException;

}
