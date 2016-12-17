package io.dico.dicore.util.playerset;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.HashSet;

public class UpdatedPlayerSet extends HashSet<Player> implements PlayerSet, Listener {
    
    public UpdatedPlayerSet(Plugin plugin) {
        PlayerQuitEvent.getHandlerList().register(new RegisteredListener(this, this::onPlayerQuit, EventPriority.HIGH, plugin, false));
    }
    
    public void unregister() {
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
    
    private void onPlayerQuit(Listener listener, Event event) {
        remove(((PlayerQuitEvent) event).getPlayer());
    }
    
    @Override
    public boolean isLazy() {
        return false;
    }
}
