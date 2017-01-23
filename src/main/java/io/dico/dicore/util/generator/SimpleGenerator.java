package io.dico.dicore.util.generator;

import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SimpleGenerator<T> implements Generator<T> {
    private static final ThreadLocal<SimpleGenerator> generatorLocal = new ThreadLocal<>();
    private static final ThreadGroup threadGroup = new ThreadGroup("Generators");
    
    private final Thread thread;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private volatile T next;
    private volatile boolean hasYield = false;
    private volatile boolean hasNext = false;
    
    public SimpleGenerator() {
        thread = new Thread(threadGroup, this::thread);
        thread.setDaemon(true);
        thread.start();
    }
    
    private void thread() {
        lock.lock();
        generatorLocal.set(this);
        condition.awaitUninterruptibly();
        try {
            run();
        } finally {
            hasNext = false;
            condition.signal();
            lock.unlock();
            generatorLocal.remove();
        }
    }
    
    @Override
    public synchronized boolean hasNext() {
        if (!hasYield) {
            lock.lock();
            condition.signal();
            condition.awaitUninterruptibly();
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
        return next;
    }
    
    protected abstract void run();
    
    protected void yield(T value) {
        next = value;
        hasNext = true;
        condition.signal();
        condition.awaitUninterruptibly();
    }
    
    @Override
    public boolean isPreparingGenerator() {
        return false;
    }
    
    @SuppressWarnings("unchecked")
    public static void doYield(Object value) {
        generatorLocal.get().yield(value);
    }
    
    public static <T> SimpleGenerator<T> generator(Runnable method) {
        return new SimpleGenerator<T>() {
            @Override
            protected void run() {
                method.run();
            }
        };
    }
    
}
