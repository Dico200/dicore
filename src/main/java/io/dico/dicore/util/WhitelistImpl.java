package io.dico.dicore.util;

import java.util.Set;

public class WhitelistImpl implements Whitelist {

    private final Set list;
    private final boolean blacklist;

    public WhitelistImpl(Set list, boolean blacklist) {
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
}
