package io.dico.dicore.nms.impl.v1_7_R4;

import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;
import io.dico.dicore.nms.nbt.NBTType;
import io.dico.dicore.util.Reflection;
import net.minecraft.server.v1_7_R4.NBTBase;
import net.minecraft.server.v1_7_R4.NBTTagList;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

class NBTListImpl extends AbstractList<Object> implements NBTList {
    final NBTTagList nbtList;
    final List<NBTBase> list;
    
    NBTListImpl(NBTTagList list) {
        this.nbtList = list;
        this.list = Reflection.getValueInField(NBTTagList.class, "list", list);
    }
    
    NBTListImpl() {
        this(new NBTTagList());
    }
    
    @Override
    public Object get(int index) {
        if (index >= nbtList.size() || index < 0) {
            return null;
        }
        return list.get(index);
    }
    
    @Override
    public int size() {
        return nbtList.size();
    }
    
    @Override
    public boolean add(Object o) {
        int size = nbtList.size();
        nbtList.add(ConverterImpl.toNMS(o));
        return nbtList.size() != size;
    }
    
    @Override
    public Object set(int index, Object element) {
        if (index >= nbtList.size() || index < 0) {
            return null;
        }
        Object previous = get(index);
        list.set(index, ConverterImpl.toNMS(element));
        return previous;
    }
    
    @Override
    public Object remove(int index) {
        if (index >= nbtList.size() || index < 0) {
            return null;
        }
        return ConverterImpl.fromNMS(list.remove(index));
    }
    
    @Override
    public NBTType getElementType() {
        return ConverterImpl.getElementType(nbtList.d());
    }
    
    @Override
    public NBTMap getMap(int index, NBTMap absent) {
        Objects.requireNonNull(absent);
        try {
            NBTMap result = (NBTMap) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }
    
    @Override
    public NBTList getList(int index, NBTList absent) {
        Objects.requireNonNull(absent);
        try {
            NBTList result = (NBTList) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }
    
    @Override
    public int[] getIntArray(int index, int[] absent) {
        Objects.requireNonNull(absent);
        try {
            int[] result = (int[]) get(index);
            return result == null ? absent : result;
        } catch (ClassCastException e) {
            return absent;
        }
    }
    
    @Override
    public byte[] getByteArray(int index, byte[] absent) {
        Objects.requireNonNull(absent);
        try {
            byte[] result = (byte[]) get(index);
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
        Objects.requireNonNull(absent);
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
    
    @Override
    public NBTMap getMap(int index, Supplier<NBTMap> absent) {
        Objects.requireNonNull(absent);
        try {
            NBTMap result = (NBTMap) get(index);
            return result == null ? absent.get() : result;
        } catch (ClassCastException e) {
            return absent.get();
        }
    }
    
    @Override
    public NBTMap getPresentMap(int index, NBTMap absent) {
        Objects.requireNonNull(absent);
        NBTMap result = null;
        try {
            result = (NBTMap) get(index);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent;
            set(index, result);
        }
        return result;
    }
    
    @Override
    public NBTMap getPresentMap(int index, Supplier<NBTMap> absent) {
        Objects.requireNonNull(absent);
        NBTMap result = null;
        try {
            result = (NBTMap) get(index);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent.get();
            set(index, result);
        }
        return result;
    }
    
    @Override
    public NBTList getPresentList(int index, NBTList absent) {
        Objects.requireNonNull(absent);
        NBTList result = null;
        try {
            result = (NBTList) get(index);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent;
            set(index, result);
        }
        return result;
    }
    
    @Override
    public NBTList getPresentList(int index, Supplier<NBTList> absent) {
        Objects.requireNonNull(absent);
        NBTList result = null;
        try {
            result = (NBTList) get(index);
        } catch (ClassCastException ignored) {
        }
        if (result == null) {
            result = absent.get();
            set(index, result);
        }
        return result;
    }
    
    @Override
    public NBTList getList(int index, Supplier<NBTList> absent) {
        Objects.requireNonNull(absent);
        try {
            NBTList result = (NBTList) get(index);
            return result == null ? absent.get() : result;
        } catch (ClassCastException e) {
            return absent.get();
        }
    }
}
