package io.dico.dicore.nms.impl.V1_8_R3.nbt;

import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;
import io.dico.dicore.nms.nbt.NBTType;
import net.minecraft.server.v1_8_R3.*;

import java.util.*;

public class NBTListImpl extends AbstractList<Object> implements NBTList {

    public final NBTTagList list;

    public NBTListImpl(NBTTagList list) {
        this.list = list;
    }

    public NBTListImpl() {
        this(new NBTTagList());
    }

    @Override
    public Object get(int index) {
        return Converter.fromNMS(list.g(index));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(Object o) {
        int size = list.size();
        list.add(Converter.toNMS(o));
        return list.size() != size;
    }

    @Override
    public Object set(int index, Object element) {
        Object previous = get(index);
        list.a(index, Converter.toNMS(element));
        return previous;
    }

    @Override
    public Object remove(int index) {
        return Converter.fromNMS(list.a(index));
    }

    @Override
    public NBTType getElementType() {
        return Converter.getElementType(list.f());
    }

    @Override
    public NBTMap getMap(int index, NBTMap absent) {
        try {
            NBTMap result = (NBTMap) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public NBTList getList(int index, NBTList absent) {
        try {
            NBTList result = (NBTList) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public int[] getIntArray(int index, int[] absent) {
        try {
            int[] result = (int[]) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public double getDouble(int index, double absent) {
        try {
            Double result = (Double) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public float getFloat(int index, float absent) {
        try {
            Float result = (Float) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public String getString(int index, String absent) {
        try {
            String result = (String) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public long getLong(int index, long absent) {
        try {
            Long result = (Long) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public int getInt(int index, int absent) {
        try {
            Integer result = (Integer) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }

    @Override
    public short getShort(int index, int absent) {
        try {
            Short result = (Short) get(index);
            return result == null ? (short) absent : result;
        } catch (ClassCastException e) {
            return (short) absent;
        }
    }

    @Override
    public byte getByte(int index, int absent) {
        try {
            Byte result = (Byte) get(index);
            return result == null ? (byte) absent : result;
        } catch (ClassCastException e) {
            return (byte) absent;
        }
    }

}
