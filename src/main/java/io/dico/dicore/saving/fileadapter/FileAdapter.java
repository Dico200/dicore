package io.dico.dicore.saving.fileadapter;

import java.io.File;
import java.io.IOException;

public abstract class FileAdapter<T> {
    
    public static File fileAt(String path, boolean createIfAbsent) throws IOException {
        File file = new File(path);
        if (createIfAbsent && !file.exists()) {
            File parent = file.getParentFile();
            if (parent == null || !(parent.exists() || parent.mkdirs()) || !file.createNewFile()) {
                throw new IOException("Failed to create file " + file.getAbsolutePath());
            }
        }
        return file;
    }
    
    protected abstract void onErrorLoad(Exception ex);
    
    protected abstract void onErrorSave(Exception ex);
    
    protected abstract T fallback();
    
    public abstract void saveUnsafe(T object, String path) throws Exception;
    
    public void save(T object, String path) {
        try {
            saveUnsafe(object, path);
        } catch (Exception ex) {
            onErrorSave(ex);
        }
    }
    
    public abstract T loadUnsafe(String path) throws Exception;
    
    public T load(String path) {
        try {
            return loadUnsafe(path);
        } catch (Exception ex) {
            onErrorLoad(ex);
            return fallback();
        }
    }
    
}
