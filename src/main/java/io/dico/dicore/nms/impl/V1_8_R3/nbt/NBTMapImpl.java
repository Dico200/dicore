package io.dico.dicore.nms.impl.V1_8_R3.nbt;

import com.google.common.collect.Maps;
import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;
import io.dico.dicore.util.Reflection;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class NBTMapImpl implements NBTMap {

    public final NBTTagCompound base;
    private final Map<String, Object> map;

    public NBTMapImpl(NBTTagCompound base) {
        this.base = base;
        Map<String, NBTBase> map = Reflection.getValueInField(NBTTagCompound.class, "map", base);
        this.map = Maps.transformValues(map, Converter::fromNMS);
    }

    public NBTMapImpl() {
        this(new NBTTagCompound());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        Object previous = get(key);
        base.set(key, Converter.toNMS(value));
        return previous;
    }

    @Override
    public Object remove(Object key) {
        Object previous = get(key);
        base.remove((String) key);
        return previous;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        for (Entry<? extends String, ?> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public NBTMap getMap(Object key, NBTMap absent) {
        try {
            NBTMap result = (NBTMap) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public NBTList getList(Object key, NBTList absent) {
        try {
            NBTList result = (NBTList) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public int[] getIntArray(Object key, int[] absent) {
        try {
            int[] result = (int[]) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public double getDouble(Object key, double absent) {
        try {
            Double result = (Double) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public float getFloat(Object key, float absent) {
        try {
            Float result = (Float) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public long getLong(Object key, long absent) {
        try {
            Long result = (Long) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public int getInt(Object key, int absent) {
        try {
            Integer result = (Integer) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public short getShort(Object key, int absent) {
        try {
            Short result = (Short) get(key);
            return result == null ? (short) absent : result;
        } catch (ClassCastException e) {
            return (short) absent;
        }
    }

    @Override
    public byte getByte(Object key, int absent) {
        try {
            Byte result = (Byte) get(key);
            return result == null ? (byte) absent : result;
        } catch (ClassCastException e) {
            return (byte) absent;
        }
    }

    @Override
    public byte[] getByteArray(Object key, byte[] absent) {
        try {
            byte[] result = (byte[]) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

}
