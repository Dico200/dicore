package io.dico.dicore.saving.fileadapter;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class GsonFileAdapter<T> extends FileAdapter<T> {
    private final Type typeOfT;
    private final Gson gson;

    public GsonFileAdapter(Type typeOfT, Gson gson) {
        this.typeOfT = Objects.requireNonNull(typeOfT, "typeOfT");
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    protected T fallback() {
        return null;
    }

    @Override
    public void saveUnsafe(T object, String path) throws Exception {
        try (FileWriter fileWriter = new FileWriter(fileAt(path, true))) {
            gson.toJson(object, fileWriter);
        }
    }

    @Override
    public T loadUnsafe(String path) throws Exception {
        try (FileReader fileReader = new FileReader(fileAt(path, false))) {
            return gson.fromJson(fileReader, typeOfT);
        }
    }

    public static <T> GsonFileAdapter<T> create(Type typeOfT, Gson gson, Consumer<? super Exception> onError) {
        return create(typeOfT, gson, onError, onError);
    }

    public static <T> GsonFileAdapter<T> create(Type typeOfT, Gson gson, Consumer<? super Exception> onLoad, Consumer<? super Exception> onSave) {
        return new GsonFileAdapter<T>(typeOfT, gson) {
            @Override
            protected void onErrorLoad(Exception ex) {
                onLoad.accept(ex);
            }

            @Override
            protected void onErrorSave(Exception ex) {
                onSave.accept(ex);
            }
        };
    }

}
