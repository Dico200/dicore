package io.dico.dicore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

public class OnEditCollection<E> extends ArrayList<E> {

    private final Runnable onEdit;

    public OnEditCollection(Collection<E> delegate, Runnable onEdit) {
        addAll(delegate);
        this.onEdit = onEdit;
    }

    @Override
    public E set(int index, E element) {
        return handle(super.set(index, element));
    }

    @Override
    public boolean add(E e) {
        return handle(super.add(e));
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        onEdit.run();
    }

    @Override
    public E remove(int index) {
        return handle(super.remove(index));
    }

    @Override
    public boolean remove(Object o) {
        return handle(super.remove(o));
    }

    @Override
    public void clear() {
        super.clear();
        onEdit.run();
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.sort(c);
        onEdit.run();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return handle(super.removeAll(c));
    }

    @Override
    public boolean retainAll(Collection<?> c) {
       return handle(super.retainAll(c));
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return handle(super.addAll(c));
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return handle(super.addAll(index, c));
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return super.removeIf(filter);
    }

    private <T> T handle(T result) {
        onEdit.run();
        return result;
    }
}
