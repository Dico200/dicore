package io.dico.dicore.nms.nbt;

import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.impl.v1_8_R3.nbt.NBTList_v1_8_R3;
import io.dico.dicore.nms.impl.v1_8_R3.nbt.NBTMap_v1_8_R3;
import io.dico.dicore.nms.impl.unknown.nbt.NBTList_UNKNOWN;
import io.dico.dicore.nms.impl.unknown.nbt.NBTMap_UNKNOWN;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum NBTType {
    BYTE(Byte.class),
    SHORT(Short.class),
    INT(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    STRING(String.class),
    BYTE_ARRAY(byte[].class),
    INT_ARRAY(int[].class),
    LIST(nbtListClass()),
    MAP(nbtMapClass());
    
    public static NBTType valueOf(Class<?> clazz) {
        NBTType result = map.get(clazz);
        if (result == null) {
            if (clazz == NBTMap.class) {
                return MAP;
            }
            if (clazz == NBTList.class) {
                return LIST;
            }
            throw new IllegalArgumentException();
        }
        return result;
    }
    
    public static NBTList newNBTList() {
        switch (NDriver.Version.getInstance()) {
            case v1_8_R3:
                return new NBTList_v1_8_R3();
            default:
                return new NBTList_UNKNOWN();
        }
    }
    
    public static NBTList newNBTList(Collection<Object> list) {
        NBTList result = newNBTList();
        result.addAll(list);
        return result;
    }
    
    public static NBTMap newNBTMap() {
        switch (NDriver.Version.getInstance()) {
            case v1_8_R3:
                return new NBTMap_v1_8_R3();
            default:
                return new NBTMap_UNKNOWN();
        }
    }
    
    public static NBTMap newNBTMap(Map<String, Object> map) {
        NBTMap result = newNBTMap();
        result.putAll(map);
        return result;
    }
    
    private static Class<? extends NBTMap> nbtMapClass() {
        switch (NDriver.Version.getInstance()) {
            case v1_8_R3:
                return NBTMap_v1_8_R3.class;
            default:
                return NBTMap_UNKNOWN.class;
        }
    }
    
    private static Class<? extends NBTList> nbtListClass() {
        switch (NDriver.Version.getInstance()) {
            case v1_8_R3:
                return NBTList_v1_8_R3.class;
            default:
                return NBTList_UNKNOWN.class;
        }
    }
    
    private static Map<Class<?>, NBTType> map;
    private final Class<?> clazz;
    
    NBTType(Class<?> clazz) {
        this.clazz = clazz;
        add();
    }
    
    private void add() {
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(clazz, this);
    }
    
    public Class<?> getType() {
        return clazz;
    }
    
}