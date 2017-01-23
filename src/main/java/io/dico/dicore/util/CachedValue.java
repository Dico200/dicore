package io.dico.dicore.util;

import java.util.function.Supplier;

public abstract class CachedValue<T> {
    
    private boolean changed = true;
    private T instance;
    
    protected abstract T update();
    
    public void changed() {
        changed = true;
    }
    
    public boolean isChanged() {
        return changed;
    }
    
    public T get() {
        if (changed) {
            refresh();
        }
        return instance;
    }
    
    public void refresh() {
        instance = update();
        changed = false;
    }
    
    public static <T> CachedValue<T> create(Supplier<T> supplier) {
        return new CachedValue<T>() {
            @Override
            protected T update() {
                return supplier.get();
            }
        };
    }
    
}
