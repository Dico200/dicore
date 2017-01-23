package io.dico.dicore.util.generator;

import java.util.Iterator;

public interface Generator<T> extends Iterable<T>, Iterator<T> {
    ThreadGroup THREAD_GROUP = new ThreadGroup("Generators");
    
    @Override
    default Iterator<T> iterator() {
        return this;
    }
    
    boolean isPreparingGenerator();
    
}
