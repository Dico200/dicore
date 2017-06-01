package io.dico.dicore.nms.impl.v1_8_R3;

import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;
import io.dico.dicore.nms.nbt.NBTType;
import net.minecraft.server.v1_8_R3.*;

final class ConverterImpl {
    
    private ConverterImpl() {
        throw new UnsupportedOperationException();
    }
    
    public static Object fromNMS(NBTBase base) {
        if (base == null) {
            return null;
        }
        switch (base.getTypeId()) {
            case 0:
                return null; //NBTTagEnd
            case 1:
                return ((NBTTagByte) base).f();
            case 2:
                return ((NBTTagShort) base).e();
            case 3:
                return ((NBTTagInt) base).d();
            case 4:
                return ((NBTTagLong) base).c();
            case 5:
                return ((NBTTagFloat) base).h();
            case 6:
                return ((NBTTagDouble) base).g();
            case 7:
                return ((NBTTagByteArray) base).c();
            case 8:
                return ((NBTTagString) base).a_();
            case 9:
                return new NBTListImpl((NBTTagList) base);
            case 10:
                return new NBTMapImpl((NBTTagCompound) base);
            case 11:
                return ((NBTTagIntArray) base).c();
            default:
                return null;
        }
    }
    
    public static NBTType getElementType(int typeId) {
        switch (typeId) {
            case 1:
                return NBTType.BYTE;
            case 2:
                return NBTType.SHORT;
            case 3:
                return NBTType.INT;
            case 4:
                return NBTType.LONG;
            case 5:
                return NBTType.FLOAT;
            case 6:
                return NBTType.DOUBLE;
            case 7:
                return NBTType.BYTE_ARRAY;
            case 8:
                return NBTType.STRING;
            case 9:
                return NBTType.LIST;
            case 10:
                return NBTType.MAP;
            case 11:
                return NBTType.INT_ARRAY;
            default:
                return null;
        }
    }
    
    public static NBTBase toNMS(Object object) {
        if (object == null) {
            return null;
        }
        switch (NBTType.valueOf(object.getClass())) {
            case BYTE:
                return new NBTTagByte((byte) object);
            case SHORT:
                return new NBTTagShort((short) object);
            case INT:
                return new NBTTagInt((int) object);
            case LONG:
                return new NBTTagLong((long) object);
            case FLOAT:
                return new NBTTagFloat((float) object);
            case DOUBLE:
                return new NBTTagDouble((double) object);
            case STRING:
                return new NBTTagString((String) object);
            case BYTE_ARRAY:
                return new NBTTagByteArray((byte[]) object);
            case INT_ARRAY:
                return new NBTTagIntArray((int[]) object);
            case LIST:
                if (object == NBTList.EMPTY) {
                    return new NBTTagList();
                }
                if (object instanceof NBTListImpl) {
                    return ((NBTListImpl) object).list;
                }
                throw new IllegalArgumentException();
            case MAP:
                if (object == NBTMap.EMPTY) {
                    return new NBTTagCompound();
                }
                if (object instanceof NBTMapImpl) {
                    return ((NBTMapImpl) object).base;
                }
                throw new IllegalArgumentException();
            default:
                return null;
        }
    }
    
}
