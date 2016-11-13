package io.dico.dicore.util;

import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Whitelist extends Predicate<Object> {

    Whitelist EVERYTHING = item -> true;
    Whitelist NOTHING = item -> false;

    static Whitelist only(Object item) {
        return item::equals;
    }

    static Whitelist not(Object item) {
        return o -> !item.equals(o);
    }

    static Whitelist only(Object item1, Object item2) {
        return item -> item1.equals(item) || item2.equals(item);
    }

    static Whitelist not(Object item1, Object item2) {
        return item -> !(item1.equals(item) || item2.equals(item));
    }

    static Whitelist only(Object[] objects) {
        return new SetBasedWhitelist(objects, false);
    }

    static Whitelist not(Object[] objects) {
        return new SetBasedWhitelist(objects, true);
    }

    static Whitelist fromConfig(ConfigurationSection section, Function<String, ?> parser) {
        if (section == null) {
            return NOTHING;
        } else {
            boolean blacklist = section.getBoolean("blacklist", false);
            Set list = section.getStringList("listed").stream().map(parser).filter(o -> o != null).collect(Collectors.toSet());
            return new SetBasedWhitelist(list, blacklist);
        }
    }

    @Override
    default boolean test(Object o) {
        return isWhitelisted(o);
    }

    boolean isWhitelisted(Object o);
}

