package io.dico.dicore.util.task;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class IteratorTask<T> extends BaseTask<T> {

    private final Iterator<? extends T> iterator;

    @SuppressWarnings("unchecked")
    public IteratorTask(Iterable<? extends T> iterable, boolean clone) {
        if (clone) {
            Collection<T> collection;
            if (!(iterable instanceof Collection)) {
                collection = new LinkedList<>();
                for (T next : iterable) {
                    collection.add(next);
                }
            } else {
                collection = new ArrayList<>((Collection) iterable);
            }
            iterator = collection.iterator();
        } else {
            iterator = iterable.iterator();
        }
    }

    public IteratorTask(Iterable<? extends T> iterable) {
        this(iterable, false);
    }

    @Override
    protected T supply() {
        return iterator.next();
    }

    protected void remove() {
        iterator.remove();
    }

    // One argument: The processed object

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, Consumer<T> processor) {
        return create(iterable, false, processor);
    }

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, boolean clone, Consumer<T> processor) {
        return create(iterable, clone, object -> {
            processor.accept(object);
            return true;
        });
    }

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, Predicate<T> processor) {
        return create(iterable, false, processor);
    }

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, boolean clone, Predicate<T> processor) {
        return new IteratorTask<T>(iterable, clone) {
            @Override
            protected boolean process(T object) {
                return processor.test(object);
            }
        };
    }

    // Two arguments: the processed object, and a runnable to remove it from the iterator.

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, BiConsumer<T, Runnable> processor) {
        return create(iterable, false, processor);
    }

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, boolean clone, BiConsumer<T, Runnable> processor) {
        return create(iterable, clone, (object, runnable) -> {
            processor.accept(object, runnable);
            return true;
        });
    }

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, BiPredicate<T, Runnable> processor) {
        return create(iterable, false, processor);
    }

    public static <T> IteratorTask<T> create(Iterable<? extends T> iterable, boolean clone, BiPredicate<T, Runnable> processor) {
        return new IteratorTask<T>(iterable, clone) {
            @Override
            protected boolean process(T object) {
                return processor.test(object, this::remove);
            }
        };
    }

}
