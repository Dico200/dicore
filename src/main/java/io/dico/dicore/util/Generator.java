package io.dico.dicore.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Generator<T> implements Iterable<T>, Iterator<T> {
    private static final ThreadLocal<Generator> generatorLocal = new ThreadLocal<>();
    private static final ThreadGroup threadGroup = new ThreadGroup("Generators");
    private final Runnable function;
    private final Thread thread;
    private volatile T nextValue;
    private volatile boolean hasNext = false;
    private volatile boolean hasYield = false;
    private final Lock lock = new ReentrantLock();
    private final Condition startCondition = lock.newCondition();
    private final Condition nextCondition = lock.newCondition();
    private final Condition valueCondition = lock.newCondition();
    
    public Generator(Runnable function) {
        this.function = function;
        this.thread = new Thread(threadGroup, this::run);
        lock.lock();
        thread.setDaemon(true);
        thread.start();
        startCondition.awaitUninterruptibly();
        lock.unlock();
    }
    
    private void run() {
        lock.lock();
        startCondition.signal();
        nextCondition.awaitUninterruptibly();
        try {
            generatorLocal.set(this);
            function.run();
        } finally {
            hasNext = false;
            lock.unlock();
        }
    }
    
    public void yieldValue(T value) {
        hasNext = true;
        nextValue = value;
        valueCondition.signal();
        nextCondition.awaitUninterruptibly();
    }
    
    @Override
    public synchronized boolean hasNext() {
        if (!hasYield) {
            lock.lock();
            nextCondition.signal();
            valueCondition.awaitUninterruptibly();
            hasYield = true;
            lock.unlock();
        }
        return hasNext;
    }
    
    @Override
    public synchronized T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        hasYield = false;
        return nextValue;
    }
    
    @Override
    public Iterator<T> iterator() {
        return this;
    }
    
    public static <T, A> Generator<T> generator(Consumer<A> function, A argument) {
        return generator(() -> function.accept(argument));
    }
    
    public static <T> Generator<T> generator(Runnable function) {
        return new Generator<>(function);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void yield(T value) {
        generatorLocal.get().yieldValue(value);
    }
}