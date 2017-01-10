package io.dico.dicore.nms.impl.unknown.nbt;

import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class NBTMap_UNKNOWN implements NBTMap {

    @Override
    public NBTMap getMap(Object key, NBTMap absent) {
        return absent;
    }

    @Override
    public NBTMap getMap(Object key, Supplier<NBTMap> absent) {
        return absent.get();
    }

    @Override
    public NBTMap getPresentMap(Object key, NBTMap absent) {
        return absent;
    }

    @Override
    public NBTMap getPresentMap(Object key, Supplier<NBTMap> absent) {
        return absent.get();
    }

    @Override
    public NBTList getList(Object key, NBTList absent) {
        return absent;
    }

    @Override
    public NBTList getList(Object key, Supplier<NBTList> absent) {
        return absent.get();
    }

    @Override
    public NBTList getPresentList(Object key, NBTList absent) {
        return absent;
    }

    @Override
    public NBTList getPresentList(Object key, Supplier<NBTList> absent) {
        return absent.get();
    }

    @Override
    public int[] getIntArray(Object key, int[] absent) {
        return absent;
    }

    @Override
    public byte[] getByteArray(Object key, byte[] absent) {
        return absent;
    }

    @Override
    public double getDouble(Object key, double absent) {
        return absent;
    }

    @Override
    public float getFloat(Object key, float absent) {
        return absent;
    }

    @Override
    public long getLong(Object key, long absent) {
        return absent;
    }

    @Override
    public int getInt(Object key, int absent) {
        return absent;
    }

    @Override
    public short getShort(Object key, int absent) {
        return (short) absent;
    }

    @Override
    public byte getByte(Object key, int absent) {
        return (byte) absent;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }

    @Override
    public Collection<Object> values() {
        return Collections.emptyList();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return Collections.emptySet();
    }

}
