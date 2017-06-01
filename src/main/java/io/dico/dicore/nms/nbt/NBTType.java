package io.dico.dicore.nms.nbt;

import io.dico.dicore.nms.NDriver;

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
    LIST(NBTList.class),
    MAP(NBTMap.class);
    
    public static NBTType valueOf(Class<?> clazz) {
        NBTType result = map.get(clazz);
        if (result == null) {
            if (NBTMap.class.isAssignableFrom(clazz)) {
                map.put(clazz, MAP);
                return MAP;
            }
            if (NBTList.class.isAssignableFrom(clazz)) {
                map.put(clazz, LIST);
                return LIST;
            }
            throw new IllegalArgumentException();
        }
        return result;
    }
    
    public static NBTList newNBTList() {
        return NDriver.getInstance().newNbtList();
    }
    
    public static NBTList newNBTList(Collection<Object> list) {
        NBTList result = newNBTList();
        result.addAll(list);
        return result;
    }
    
    public static NBTMap newNBTMap() {
        return NDriver.getInstance().newNbtMap();
    }
    
    public static NBTMap newNBTMap(Map<String, Object> map) {
        NBTMap result = newNBTMap();
        result.putAll(map);
        return result;
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