package io.dico.dicore.util.exceptions;

public class Exceptions {

    public static <T> T supplySafeIgnoreNullPointer(UnsafeSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (NullPointerException ignored) {
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public static void runSafeIgnoreNullPointer(UnsafeRunnable runnable) {
        try {
            runnable.run();
        } catch (NullPointerException ignored) {
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static <T> T supplySafe(UnsafeSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public static void runSafe(UnsafeRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}

