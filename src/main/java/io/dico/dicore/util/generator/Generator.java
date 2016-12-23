package io.dico.dicore.util.generator;

import java.util.Iterator;

public interface Generator<T> extends Iterable<T>, Iterator<T> {

    @Override
    default Iterator<T> iterator() {
        return this;
    }

    boolean isPreparingGenerator();

}
