package io.dico.dicore.saving;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public class JsonFileAdapter<T> {

    private final Type typeOfT;
    private final Gson gson;
    private final Consumer<Throwable> onErrorLoad;
    private final Consumer<Throwable> onErrorSave;

    public JsonFileAdapter(Type typeOfT, Gson gson, Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave) {
        this.typeOfT = typeOfT;
        this.gson = gson;
        this.onErrorLoad = onErrorLoad != null ? onErrorLoad : t -> {};
        this.onErrorSave = onErrorSave != null ? onErrorSave : t -> {};
    }

    public JsonFileAdapter(TypeToken<T> typeOfT, Gson gson, Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave) {
        this.typeOfT = typeOfT.getType();
        this.gson = gson;
        this.onErrorLoad = onErrorLoad != null ? onErrorLoad : t -> {};
        this.onErrorSave = onErrorSave != null ? onErrorSave : t -> {};
    }

    public void save(T object, File file) {
        PrintWriter writer = null;

        try {
            if (file.exists() || file.createNewFile() || (file.getParentFile().mkdirs() && file.createNewFile())) {
                writer = new PrintWriter(file);
                String json = gson.toJson(object);
                writer.write(json);
            } else {
                throw new IOException("Failed to create file " + file.getAbsolutePath());
            }
        } catch (Throwable t) {
            onErrorSave.accept(t);
        } finally {
            if (writer != null) writer.close();
        }
    }

    public void save(T object, String filename) {
        save(object, new File(filename));
    }

    public T load(final File file) {
        if (!file.exists()) {
            return null;
        }

        FileReader reader = null;
        T result = null;
        try {
            reader = new FileReader(file);
            result = gson.fromJson(reader, typeOfT);
        } catch (Throwable t) {
            onErrorLoad.accept(t);
            return null;
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException ignored) {}
        }

        return result;
    }

    public T load(String filename) {
        return load(new File(filename));
    }

}
