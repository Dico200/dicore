package io.dico.dicore.nms.nbt;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface NBTMap extends Map<String, Object> {
    
    default NBTMap getMap(Object key) {
        return getMap(key, EMPTY);
    }
    
    NBTMap getMap(Object key, NBTMap absent);
    
    NBTMap getMap(Object key, Supplier<NBTMap> absent);
    
    default NBTMap getPresentMap(Object key) {
        return getPresentMap(key, NBTType::newNBTMap);
    }
    
    NBTMap getPresentMap(Object key, NBTMap absent);
    
    NBTMap getPresentMap(Object key, Supplier<NBTMap> absent);
    
    default NBTList getList(Object key) {
        return getList(key, NBTList.EMPTY);
    }
    
    NBTList getList(Object key, NBTList absent);
    
    NBTList getList(Object key, Supplier<NBTList> absent);
    
    default NBTList getPresentList(Object key) {
        return getPresentList(key, NBTType::newNBTList);
    }
    
    NBTList getPresentList(Object key, NBTList absent);
    
    NBTList getPresentList(Object key, Supplier<NBTList> absent);
    
    String getString(Object key, String absent);
    
    String getString(Object key, Supplier<String> absent);
    
    default String getString(Object key) {
        return getString(key, (String) null);
    }
    
    default int[] getIntArray(Object key) {
        return getIntArray(key, new int[0]);
    }
    
    int[] getIntArray(Object key, int[] absent);
    
    default byte[] getByteArray(Object key) {
        return getByteArray(key, new byte[0]);
    }
    
    byte[] getByteArray(Object key, byte[] absent);
    
    default double getDouble(Object key) {
        return getDouble(key, 0D);
    }
    
    double getDouble(Object key, double absent);
    
    default float getFloat(Object key) {
        return getFloat(key, 0F);
    }
    
    float getFloat(Object key, float absent);
    
    default long getLong(Object key) {
        return getLong(key, 0L);
    }
    
    long getLong(Object key, long absent);
    
    default int getInt(Object key) {
        return getInt(key, 0);
    }
    
    int getInt(Object key, int absent);
    
    default short getShort(Object key) {
        return getShort(key, 0);
    }
    
    short getShort(Object key, int absent);
    
    default byte getByte(Object key) {
        return getByte(key, 0);
    }
    
    byte getByte(Object key, int absent);
    
    NBTMap EMPTY = new NBTMap() {
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
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(Map<? extends String, ?> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
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
        
        @Override
        public NBTMap getMap(Object key, NBTMap absent) {
            return absent;
        }
        
        @Override
        public NBTList getList(Object key, NBTList absent) {
            return absent;
        }
        
        @Override
        public int[] getIntArray(Object key, int[] absent) {
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
        public byte[] getByteArray(Object key, byte[] absent) {
            return absent;
        }
        
        @Override
        public NBTMap getMap(Object key, Supplier<NBTMap> absent) {
            return absent.get();
        }
        
        @Override
        public NBTMap getPresentMap(Object key, NBTMap absent) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public NBTMap getPresentMap(Object key, Supplier<NBTMap> absent) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public NBTList getList(Object key, Supplier<NBTList> absent) {
            return absent.get();
        }
        
        @Override
        public NBTList getPresentList(Object key, NBTList absent) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public NBTList getPresentList(Object key, Supplier<NBTList> absent) {
            throw new UnsupportedOperationException();
        }
    
        @Override
        public String getString(Object key, String absent) {
            return absent;
        }
    
        @Override
        public String getString(Object key, Supplier<String> absent) {
            return absent.get();
        }
    };
}
