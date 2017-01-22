package io.dico.dicore.util.generator;

import java.util.NoSuchElementException;

public abstract class AwesomeGenerator<Element> implements Generator<Element> {
    private static final ThreadLocal<AwesomeGenerator> generatorLocal = new ThreadLocal<>();
    private final Thread thread;
    private final Object condition = new Object();
    private volatile Element next;
    private volatile boolean hasNext;
    private volatile boolean hasYield;
    
    public AwesomeGenerator() {
        thread = new Thread(THREAD_GROUP, this::thread);
        thread.setDaemon(true);
        thread.start();
    }
    
    private void thread() {
        synchronized (condition) {
            generatorLocal.set(this);
            condition.notify();
            doWait();
            try {
                run();
            } finally {
                hasNext = false;
                condition.notify();
                generatorLocal.remove();
            }
        }
        /*
        generatorLocal.set(this);
        
        condition.awaitUninterruptibly();
        try {
            run();
        } finally {
            hasNext = false;
            condition.signal();
            condition.unlock();
            generatorLocal.remove();
        }
        */
    }
    
    @Override
    public synchronized boolean hasNext() {
        if (!hasYield) {
            synchronized (condition) {
                doWait();
                condition.notify();
                doWait();
                hasYield = true;
            }
        }
        return hasNext;
    }
    
    @Override
    public synchronized Element next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        hasYield = false;
        return next;
    }
    
    protected abstract void run();
    
    protected void yield(Element element) {
        next = element;
        hasNext = true;
        condition.notify();
        doWait();
    }
    
    @SuppressWarnings("unchecked")
    public static void doYield(Object element) {
        generatorLocal.get().yield(element);
    }
    
    public static <Element> AwesomeGenerator<Element> generator(Runnable method) {
        return new AwesomeGenerator<Element>() {
            @Override
            protected void run() {
                method.run();
            }
        };
    }
    
    @Override
    public boolean isPreparingGenerator() {
        return false;
    }
    
    private void doWait() {
        try {
            condition.wait();
        } catch (InterruptedException ex) {
            doWait();
        }
    }
    
}
