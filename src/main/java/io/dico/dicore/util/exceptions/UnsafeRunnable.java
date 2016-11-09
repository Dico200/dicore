package io.dico.dicore.util.exceptions;

@FunctionalInterface
public interface UnsafeRunnable {

    void run() throws Throwable;

}
