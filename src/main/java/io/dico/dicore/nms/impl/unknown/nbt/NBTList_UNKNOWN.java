package io.dico.dicore.nms.impl.unknown.nbt;

import com.google.common.collect.Iterators;
import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;
import io.dico.dicore.nms.nbt.NBTType;

import java.util.*;
import java.util.function.Supplier;

public class NBTList_UNKNOWN implements NBTList {

    @Override
    public NBTType getElementType() {
        return NBTType.LIST;
    }

    @Override
    public NBTMap getMap(int index, NBTMap absent) {
        return absent;
    }

    @Override
    public NBTMap getMap(int index, Supplier<NBTMap> absent) {
        return absent.get();
    }

    @Override
    public NBTMap getPresentMap(int index, NBTMap absent) {
        return absent;
    }

    @Override
    public NBTMap getPresentMap(int index, Supplier<NBTMap> absent) {
        return absent.get();
    }

    @Override
    public NBTList getPresentList(int index, NBTList absent) {
        return absent;
    }

    @Override
    public NBTList getPresentList(int index, Supplier<NBTList> absent) {
        return absent.get();
    }

    @Override
    public NBTList getList(int index, NBTList absent) {
        return absent;
    }

    @Override
    public NBTList getList(int index, Supplier<NBTList> absent) {
        return absent.get();
    }

    @Override
    public int[] getIntArray(int index, int[] absent) {
        return new int[0];
    }

    @Override
    public byte[] getByteArray(int index, byte[] absent) {
        return new byte[0];
    }

    @Override
    public double getDouble(int index, double absent) {
        return 0;
    }

    @Override
    public float getFloat(int index, float absent) {
        return 0;
    }

    @Override
    public String getString(int index, String absent) {
        return absent;
    }

    @Override
    public long getLong(int index, long absent) {
        return absent;
    }

    @Override
    public int getInt(int index, int absent) {
        return absent;
    }

    @Override
    public short getShort(int index, int absent) {
        return (short) absent;
    }

    @Override
    public byte getByte(int index, int absent) {
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
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return a;
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public Object set(int index, Object element) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return -1;
    }

    @Override
    public ListIterator<Object> listIterator() {
        return new ListIterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Object previous() {
                throw new NoSuchElementException();
            }

            @Override
            public int nextIndex() {
                return -1;
            }

            @Override
            public int previousIndex() {
                return -1;
            }

            @Override
            public void remove() {
                throw new IllegalStateException();
            }

            @Override
            public void set(Object o) {
                throw new IllegalStateException();
            }

            @Override
            public void add(Object o) {

            }
        };
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return listIterator();
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        throw new IndexOutOfBoundsException();
    }
}
