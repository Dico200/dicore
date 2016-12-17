package io.dico.dicore.util.playerset;

import org.bukkit.entity.Player;

import java.util.HashSet;

public class LazyPlayerSet extends HashSet<Player> implements PlayerSet {
    
    @Override
    public boolean contains(Object o) {
        boolean result = super.contains(o);
        if (result && !((Player) o).isOnline()) {
            return !remove(o);
        }
        return result;
    }
    
    @Override
    public boolean isLazy() {
        return true;
    }
}
