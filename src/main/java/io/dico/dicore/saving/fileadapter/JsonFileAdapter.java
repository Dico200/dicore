package io.dico.dicore.saving.fileadapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.JsonLoadable;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class JsonFileAdapter<T extends JsonLoadable> extends FileAdapter<T> {
    private String indent = "";

    public JsonFileAdapter<T> indent(String indent) {
        if (indent == null) {
            indent = "";
        }
        this.indent = indent;
        return this;
    }

    public String indent() {
        return indent;
    }

    @Override
    public void saveUnsafe(T object, String path) throws Exception {
        try (FileWriter fileWriter = new FileWriter(fileAt(path, true));
             JsonWriter jsonWriter = new JsonWriter(fileWriter)) {
            jsonWriter.setIndent(indent);
            object.writeTo(jsonWriter);
        }
    }

    @Override
    public T loadUnsafe(String path) throws Exception {
        T object = fallback();
        try (FileReader fileReader = new FileReader(fileAt(path, false));
             JsonReader jsonReader = new JsonReader(fileReader)) {
            object.loadFrom(jsonReader);
        }
        return object;
    }

    public static <T extends JsonLoadable> JsonFileAdapter<T> create(Supplier<T> constructor, Consumer<Exception> onError) {
        return create(constructor, onError, onError);
    }

    public static <T extends JsonLoadable> JsonFileAdapter<T> create(Supplier<T> constructor, Consumer<Exception> onLoad, Consumer<Exception> onSave) {
        return new JsonFileAdapter<T>() {
            @Override
            protected void onErrorLoad(Exception ex) {
                onLoad.accept(ex);
            }

            @Override
            protected void onErrorSave(Exception ex) {
                onSave.accept(ex);
            }

            @Override
            protected T fallback() {
                return constructor.get();
            }
        };
    }

}
