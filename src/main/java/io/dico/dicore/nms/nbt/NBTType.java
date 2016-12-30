package io.dico.dicore.nms.nbt;

import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.impl.V1_8_R3.nbt.NBTListImpl;
import io.dico.dicore.nms.impl.V1_8_R3.nbt.NBTMapImpl;

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
            throw new IllegalArgumentException();
        }
        return result;
    }

    public static NBTList newNBTList() {
        switch (NDriver.Version.getInstance()) {
            case V1_8_R3:
                return new NBTListImpl();
            default:
                return null;
        }
    }

    public static NBTList newNBTList(Collection<Object> list) {
        NBTList result = newNBTList();
        result.addAll(list);
        return result;
    }

    public static NBTMap newNBTMap() {
        switch (NDriver.Version.getInstance()) {
            case V1_8_R3:
                return new NBTMapImpl();
            default:
                return null;
        }
    }

    public static NBTMap newNBTMap(Map<String, Object> map) {
        NBTMap result = newNBTMap();
        result.putAll(map);
        return result;
    }

    private static Class<? extends NBTMap> nbtMapClass() {
        switch (NDriver.Version.getInstance()) {
            case V1_8_R3:
                return NBTMapImpl.class;
            default:
                return null;
        }
    }

    private static Class<? extends NBTList> nbtListClass() {
        switch (NDriver.Version.getInstance()) {
            case V1_8_R3:
                return NBTListImpl.class;
            default:
                return null;
        }
    }

    private static final Map<Class<?>, NBTType> map = new HashMap<>();
    private final Class<?> clazz;

    NBTType(Class<?> clazz) {
        this.clazz = clazz;
        add();
    }

    private void add() {
        map.put(clazz, this);
    }

    public Class<?> getType() {
        return clazz;
    }
}