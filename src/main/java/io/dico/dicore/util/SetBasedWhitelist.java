package io.dico.dicore.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SetBasedWhitelist implements Whitelist {

    private final Set list;
    private final boolean blacklist;

    SetBasedWhitelist(Object[] array, boolean blacklist) {
        this(Arrays.asList(array), blacklist);
    }

    SetBasedWhitelist(Collection collection, boolean blacklist) {
        this(new HashSet<>(collection), blacklist);
    }

    SetBasedWhitelist(Set list, boolean blacklist) {
        this.list = list;
        this.blacklist = blacklist;
    }

    @Override
    public boolean isWhitelisted(Object o) {
        return blacklist != list.contains(o);
    }

    @Override
    public String toString() {
        return (blacklist ? "Blacklist" : "Whitelist") + "{"
                + String.join(", ", (CharSequence[]) list.stream().map(String::valueOf).toArray(String[]::new)) + "}";
    }
    
    public Set getList() {
        return list;
    }
    
    public boolean isBlacklist() {
        return blacklist;
    }
}
