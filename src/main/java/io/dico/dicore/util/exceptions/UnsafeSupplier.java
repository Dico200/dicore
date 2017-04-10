package io.dico.dicore.util.exceptions;

@FunctionalInterface
public interface UnsafeSupplier<T> {
    
    T get() throws Throwable;
    
}
