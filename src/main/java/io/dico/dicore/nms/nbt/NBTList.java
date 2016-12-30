package io.dico.dicore.nms.nbt;

import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public interface NBTList extends List<Object> {
    NBTType getElementType();

    default NBTMap getMap(int index) {
        return getMap(index, NBTMap.EMPTY);
    }

    NBTMap getMap(int index, NBTMap absent);

    default NBTList getList(int index) {
        return getList(index, EMPTY);
    }

    NBTList getList(int index, NBTList absent);

    default int[] getIntArray(int index) {
        return getIntArray(index, new int[0]);
    }

    int[] getIntArray(int index, int[] absent);

    default double getDouble(int index) {
        return getDouble(index, 0D);
    }

    double getDouble(int index, double absent);

    default float getFloat(int index) {
        return getFloat(index, 0F);
    }

    float getFloat(int index, float absent);

    default String getString(int index) {
        return getString(index, "");
    }

    String getString(int index, String absent);

    default long getLong(int index) {
        return getLong(index, 0L);
    }

    long getLong(int index, long absent);

    default int getInt(int index) {
        return getInt(index, 0);
    }

    int getInt(int index, int absent);

    default short getShort(int index) {
        return getShort(index, 0);
    }

    short getShort(int index, int absent);

    default byte getByte(int index) {
        return getByte(index, 0);
    }

    byte getByte(int index, int absent);

    NBTList EMPTY = new NBTList() {
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
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int index, Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(int index) {
            return null;
        }

        @Override
        public Object set(int index, Object element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int index, Object element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object remove(int index) {
            throw new UnsupportedOperationException();
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
            return (ListIterator<Object>) iterator();
        }

        @Override
        public ListIterator<Object> listIterator(int index) {
            return listIterator();
        }

        @Override
        public List<Object> subList(int fromIndex, int toIndex) {
            throw new IndexOutOfBoundsException("" + fromIndex);
        }

        @Override
        public NBTType getElementType() {
            return null;
        }

        @Override
        public NBTMap getMap(int index, NBTMap absent) {
            return absent;
        }

        @Override
        public int[] getIntArray(int index, int[] absent) {
            return absent;
        }

        @Override
        public double getDouble(int index, double absent) {
            return absent;
        }

        @Override
        public float getFloat(int index, float absent) {
            return absent;
        }

        @Override
        public String getString(int index, String absent) {
            return absent;
        }

        @Override
        public NBTList getList(int index, NBTList absent) {
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
    };
}
