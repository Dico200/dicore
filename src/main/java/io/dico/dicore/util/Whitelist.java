package io.dico.dicore.util;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Whitelist {

    Whitelist EVERYTHING = item -> true;
    Whitelist NOTHING = item -> false;

    static Whitelist only(Object item) {
        return item::equals;
    }

    static Whitelist not(Object item) {
        return o -> !item.equals(o);
    }

    static Whitelist fromConfig(ConfigurationSection section, Function<String, ?> parser) {
        if (section == null) {
            return NOTHING;
        } else {
            boolean blacklist = section.getBoolean("blacklist", false);
            Set list = section.getStringList("listed").stream().map(parser).filter(o -> o != null).collect(Collectors.toSet());
            return new WhitelistImpl(list, blacklist);
        }
    }

    boolean isWhitelisted(Object o);
}
