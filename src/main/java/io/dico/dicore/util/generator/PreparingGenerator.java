package io.dico.dicore.util.generator;

import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class PreparingGenerator<T> implements Generator<T> {
    private static final ThreadLocal<PreparingGenerator> threadLocal = new ThreadLocal<>();
    private static final ThreadGroup threadGroup = new ThreadGroup("Generators");

    private final Lock lock = new ReentrantLock();
    private final Synchronizer nextSync = new Synchronizer(lock.newCondition());
    private final Synchronizer returnSync = new Synchronizer(lock.newCondition());
    private final Thread thread = new Thread(threadGroup, this::thread);

    private volatile T next;
    private volatile RuntimeException thrown;
    private volatile boolean hasNext = true;
    private volatile boolean hasYield;

    public PreparingGenerator() {
        thread.setDaemon(true);
        thread.start();
    }

    private void thread() {
        try {
            lock.lock();
            threadLocal.set(this);
            run();
        } catch (RuntimeException e) {
            thrown = e;
        } finally {
            threadLocal.remove();
            nextSync.synchronize();
            hasNext = false;
            lock.unlock();
            System.out.println("Thread end");
        }
    }

    @Override
    public synchronized boolean hasNext() {
        if (!hasYield) {
            if (thrown != null) {
                try {
                    thread.interrupt();
                } catch (Exception ignored) {
                }
                throw thrown;
            }
            lock.lock();
            nextSync.synchronize();
            lock.unlock();
            hasYield = true;
        }
        return hasNext;
    }

    @Override
    public synchronized T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T next = this.next;
        lock.lock();
        returnSync.synchronize();
        lock.unlock();
        return next;
    }

    protected void yield(T value) {
        nextSync.synchronize();
        next = value;
        returnSync.synchronize();
    }

    @Override
    public boolean isPreparingGenerator() {
        return true;
    }

    protected abstract void run();

    @SuppressWarnings("unchecked")
    public static <T> void doYield(T value) {
        threadLocal.get().yield(value);
    }

    public static <T> Generator<T> generator(Runnable method) {
        return new PreparingGenerator<T>() {
            @Override
            protected void run() {
                method.run();
            }
        };
    }

    private static class Synchronizer {

        final Condition condition;
        volatile boolean waiting;

        Synchronizer(Condition condition) {
            this.condition = condition;
        }

        void synchronize() {
            if (waiting) {
                condition.signal();
            } else {
                waiting = true;
                condition.awaitUninterruptibly();
                waiting = false;
            }
        }

    }

}

