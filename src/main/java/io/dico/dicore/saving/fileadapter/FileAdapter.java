package io.dico.dicore.saving.fileadapter;

import java.io.*;

public abstract class FileAdapter<T, W extends Closeable, R extends Closeable, E extends Exception> {

    protected static File fileAt(String path, boolean createIfAbsent) throws IOException {
        File file = new File(path);
        if (createIfAbsent) {
            File parent = file.getParentFile();
            if ((parent == null || parent.exists() || parent.mkdirs()) && !file.exists() && !(file.createNewFile())) {
                throw new IOException("Failed to create file");
            }
        }
        return file;
    }

    protected abstract void onErrorLoad(Exception ex);

    protected abstract void onErrorSave(Exception ex);

    protected abstract W newWriter(String path) throws E;

    protected abstract void write(T object, W writerTo) throws E;

    protected abstract R newReader(String path) throws E;

    protected abstract T read(R readerFrom) throws E;

    public void saveUnsafe(T object, String path) throws E, IOException {
        try (W writer = newWriter(path)) {
            write(object, writer);
        }
    }

    public void save(T object, String path) {
        try {
            saveUnsafe(object, path);
        } catch (Exception ex) {
            onErrorSave(ex);
        }
    }

    public T loadUnsafe(String path) throws E, IOException {
        try (R reader = newReader(path)) {
            return read(reader);
        }
    }

    public T load(String path) {
        try {
            return loadUnsafe(path);
        } catch (Exception ex) {
            onErrorLoad(ex);
            return null;
        }
    }

}
