package io.dico.dicore.nms.impl.v1_8_R3;

import com.google.common.collect.Maps;
import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;
import io.dico.dicore.util.Reflection;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.*;
import java.util.function.Supplier;

class NBTMapImpl implements NBTMap {
    public final NBTTagCompound base;
    private final Map<String, Object> map;
    
    public NBTMapImpl(NBTTagCompound base) {
        this.base = Objects.requireNonNull(base);
        Map<String, NBTBase> map = Reflection.getValueInField(NBTTagCompound.class, "map", base);
        if (map == null) {
            map = new HashMap<>();
            Reflection.setValueInField(NBTTagCompound.class, "map", base, map);
        }
        this.map = Maps.transformValues(map, ConverterImpl::fromNMS);
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
        base.set(key, ConverterImpl.toNMS(value));
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
        Object result = get(key);
        if (result == null) {
            return absent;
        }
        try {
            return (NBTMap) result;
        } catch (ClassCastException ignored) {
        }
        try {
            return new NBTMapImpl((NBTTagCompound) result);
        } catch (ClassCastException ignored) {
        }
        return absent;
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
    
    @Override
    public NBTMap getMap(Object key, Supplier<NBTMap> absent) {
        Objects.requireNonNull(absent);
        try {
            NBTMap result = (NBTMap) get(key);
            return result == null ? absent.get() : result;
        } catch (ClassCastException e) {
            return absent.get();
        }
    }
    
    @Override
    public NBTMap getPresentMap(Object key, NBTMap absent) {
        Objects.requireNonNull(absent);
        NBTMap result = null;
        try {
            result = (NBTMap) get(key);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent;
            put((String) key, result);
        }
        return result;
    }
    
    @Override
    public NBTMap getPresentMap(Object key, Supplier<NBTMap> absent) {
        Objects.requireNonNull(absent);
        NBTMap result = null;
        try {
            result = (NBTMap) get(key);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent.get();
            put((String) key, result);
        }
        return result;
    }
    
    @Override
    public NBTList getPresentList(Object key, NBTList absent) {
        Objects.requireNonNull(absent);
        NBTList result = null;
        try {
            result = (NBTList) get(key);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent;
            put((String) key, result);
        }
        return result;
    }
    
    @Override
    public NBTList getPresentList(Object key, Supplier<NBTList> absent) {
        Objects.requireNonNull(absent);
        NBTList result = null;
        try {
            result = (NBTList) get(key);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent.get();
            put((String) key, result);
        }
        return result;
    }
    
    @Override
    public NBTList getList(Object key, Supplier<NBTList> absent) {
        Objects.requireNonNull(absent);
        try {
            NBTList result = (NBTList) get(key);
            return result == null ? absent.get() : result;
        } catch (ClassCastException e) {
            return absent.get();
        }
    }
    
    @Override
    public String getString(Object key, String absent) {
        try {
            String result = (String) get(key);
            return result == null ? absent : result;
        } catch (ClassCastException ex) {
            return absent;
        }
    }
    
    @Override
    public String getString(Object key, Supplier<String> absent) {
        Objects.requireNonNull(absent);
        try {
            String result = (String) get(key);
            return result == null ? absent.get() : result;
        } catch (ClassCastException ex) {
            return absent.get();
        }
    }
    
}
