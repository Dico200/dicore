package io.dico.dicore.util;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

public class JsonUtil {

    public static void writeItemStack(JsonWriter writer, ItemStack stack) throws IOException {
        writer.beginObject();
        writer.name("type").value(stack.getType().toString());
        if (stack.getAmount() != 1) {
            writer.name("amount").value(stack.getAmount());
        }
        if (stack.getData().getData() != 0) {
            writer.name("data").value(stack.getData().getData());
        }
        if (stack.hasItemMeta()) {

            writer.name("meta");
            writer.beginObject();
            ItemMeta meta = new DynamicEnchantmentMeta(stack.getItemMeta());

            if (meta.hasEnchants()) {
                writer.name("enchantments");
                writer.beginObject();
                for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                    writer.name(entry.getKey().getName()).value(entry.getValue());
                }
                writer.endObject();
            }

            if (meta.hasLore()) {
                writer.name("lore");
                writer.beginArray();
                for (String line : meta.getLore()) {
                    writer.value(line);
                }
                writer.endArray();
            }

            if (meta.hasDisplayName()) {
                writer.name("title").value(meta.getDisplayName());
            }

            if (meta.spigot().isUnbreakable()) {
                writer.name("unbreakable").value(true);
            }

            Set<ItemFlag> flags = meta.getItemFlags();
            if (flags != null && !flags.isEmpty()) {
                writer.name("flags");
                writer.beginArray();
                for (ItemFlag flag : flags) {
                    writer.value(flag.toString());
                }
                writer.endArray();
            }

        }

        if (stack.getDurability() != 0) {
            writer.name("durability").value(stack.getDurability());
        }
        writer.endObject();
    }

    public static ItemStack readItemStack(JsonReader reader) throws IOException {
        Object object = read(reader);
        if (object instanceof Map) {
            try {
                Map<String, Object> map = (Map<String, Object>) object;
                Material type = Material.valueOf((String) map.get("type"));
                if (type == null) return null;

                ItemStack result = new ItemStack(type);

                result.setAmount(((Number) map.getOrDefault("amount", 1)).intValue());
                result.getData().setData(((Number) map.getOrDefault("data", 0)).byteValue());
                result.setDurability(((Number) map.getOrDefault("durability", 0)).shortValue());

                object = map.get("meta");
                if (object != null && object instanceof Map) {
                    ItemMeta meta = new DynamicEnchantmentMeta(result.getItemMeta());
                    Map<String, Object> metaMap = (Map<String, Object>) object;

                    Object objectEnchantments = metaMap.get("enchantments");
                    if (objectEnchantments != null && objectEnchantments instanceof Map) {
                        Map<String, Object> enchantments = (Map<String, Object>) objectEnchantments;
                        for (Map.Entry<String, Object> entry : enchantments.entrySet()) {
                            Enchantment ench = Enchantment.getByName(entry.getKey());
                            if (ench != null) {
                                int level = ((Number) entry.getValue()).intValue();
                                meta.addEnchant(ench, level, true);
                            }
                        }
                    }

                    object = metaMap.get("lore");
                    if (object != null && object instanceof Collection) {
                        List<String> lore = (List<String>) object;
                        meta.setLore(lore);
                    }

                    object = metaMap.get("title");
                    if (object != null && object instanceof String) {
                        meta.setDisplayName((String) object);
                    }

                    object = metaMap.get("unbreakable");
                    if (object != null && object instanceof Boolean) {
                        meta.spigot().setUnbreakable((Boolean) object);
                    }

                    object = metaMap.get("flags");
                    if (object != null && object instanceof Collection) {
                        Collection<ItemFlag> flags = (Collection<ItemFlag>) object;
                        meta.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));
                    }
                }

                return result;
            } catch (Throwable t) {
                return null;
            }
        }

        return null;
    }

    public static Object read(JsonReader reader) throws IOException {

        switch (reader.peek()) {
            case BEGIN_OBJECT:
                Map<String, Object> object = new HashMap<>();

                reader.beginObject();
                while (reader.hasNext()) {
                    final String key = reader.nextName();
                    final Object value = read(reader);
                    object.put(key, value);
                }
                reader.endObject();

                return object;
            case BEGIN_ARRAY:
                Collection<Object> collection = new ArrayList<>();

                reader.beginArray();
                while (reader.hasNext()) {
                    final Object item = read(reader);
                    collection.add(item);
                }
                reader.endArray();

                return collection;
            case BOOLEAN:
                return reader.nextBoolean();
            case STRING:
                return reader.nextString();
            case NULL:
                reader.nextNull();
                return null;
            case NUMBER:
                return reader.nextDouble();
            default:
                throw new IllegalStateException();
        }

    }

    public static void insert(JsonWriter writer, Object value) throws IOException {
        if (value instanceof ConfigurationSerializable) {
            value = ((ConfigurationSerializable) value).serialize();
        }

        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            writer.beginObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                writer.name(entry.getKey());
                insert(writer, entry.getValue());
            }
            writer.endObject();
        } else if (value instanceof Iterable) {
            Iterable<Object> it = (Iterable<Object>) value;
            writer.beginArray();
            for (Object o : it) {
                insert(writer, o);
            }
            writer.endArray();
        } else if (value instanceof Number) {
            writer.value((Number) value);
        } else if (value instanceof Boolean) {
            writer.value((Boolean) value);
        } else if (value instanceof String) {
            writer.value((String) value);
        } else {
            throw new IllegalArgumentException("value not a String, Map, Number or Boolean");
        }
    }

}

abstract class JsonSpecificTokenAdapter<T, U> {

    private final String key;
    private final Class<U> clazz;
    //private final Consumer<T> consumer;
    //private final Function<T, ?> function;

    public JsonSpecificTokenAdapter(String key, Class<U> clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    public void deserialize(Map<String, Object> map, T instanceFor) {
        Object o = map.get(key);
        if (o != null) try {
            insert(clazz.cast(o), instanceFor);
        } catch (Exception ignored) {
        }
    }

    abstract void insert(U thing, T instanceInto);

    public void serialize(Map<String, Object> map, T instanceOf) {
        map.put(key, get(instanceOf));
    }

    abstract U get(T instanceFrom);

}
