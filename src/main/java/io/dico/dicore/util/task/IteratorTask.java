package io.dico.dicore.util.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class IteratorTask<T> extends BaseTask<T> {

    private final Iterator<? extends T> iterator;

    public IteratorTask(Iterable<? extends T> iterable, boolean clone) {
        if (clone && iterable instanceof Collection) {
            iterator = new ArrayList<T>((Collection<T>) iterable).iterator();
        } else {
            iterator = iterable.iterator();
        }
    }

    public IteratorTask(Iterable<? extends T> iterable) {
        this(iterable, false);
    }

    @Override
    protected T supply() {
        return iterator.hasNext() ? iterator.next() : null;
    }

    protected void remove() {
        iterator.remove();
    }

}
