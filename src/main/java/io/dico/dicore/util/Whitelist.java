package io.dico.dicore.util;

import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Whitelist extends Predicate {
    
    Whitelist EVERYTHING = item -> true;
    Whitelist NOTHING = item -> false;
    Whitelist NOT_NULL = Objects::nonNull;
    
    static <T> Predicate<T> everythingAsPredicate() {
        return (Predicate<T>) EVERYTHING;
    }
    
    static <T> Predicate<T> nothingAsPredicate() {
        return (Predicate<T>) NOTHING;
    }
    
    static <T> Predicate<T> notNullAsPredicate() {
        return (Predicate<T>) NOT_NULL;
    }
    
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
        }
        boolean blacklist = section.getBoolean("blacklist", false);
        Set list = section.getStringList("listed").stream().map(parser).filter(Objects::nonNull).collect(Collectors.toSet());
        switch (list.size()) {
            case 0:
                return blacklist ? EVERYTHING : NOTHING;
            case 1: {
                Iterator iterator = list.iterator();
                return blacklist ? not(iterator.next()) : only(iterator.next());
            }
            case 2: {
                Iterator iterator = list.iterator();
                return blacklist ? not(iterator.next(), iterator.next()) : only(iterator.next(), iterator.next());
            }
            default:
                Object item = list.iterator().next();
                if (item instanceof Enum) {
                    list = EnumSet.copyOf(list);
                }
                return new SetBasedWhitelist(list, blacklist);
        }
    }
    
    static void copyIntoConfig(ConfigurationSection target, Function<Object, String> mapper, boolean blacklist, Object... objects) {
        target.set("blacklist", blacklist);
        target.set("listed", Arrays.stream(objects).map(mapper).unordered().distinct().collect(Collectors.toList()));
    }
    
    @Override
    default boolean test(Object o) {
        return isWhitelisted(o);
    }
    
    boolean isWhitelisted(Object o);
}

