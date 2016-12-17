package io.dico.dicore.util.playerset;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface PlayerSet {
    
    static PlayerSet newUpdatedPlayerSet(Plugin plugin) {
        return new UpdatedPlayerSet(plugin);
    }
    
    static PlayerSet newLazyPlayerSet() {
        return new LazyPlayerSet();
    }
    
    boolean add(Player player);
    
    boolean contains(Object player);
    
    boolean remove(Object player);
    
    int size();
    
    default void unregister() {
    }
    
    boolean isLazy();
    
}
