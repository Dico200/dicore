package io.dico.dicore.saving.fileadapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.JsonLoadable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JsonFileAdapter<T extends JsonLoadable> extends FileAdapter<T, JsonWriter, JsonReader, IOException> {

    protected abstract T createNew();

    protected boolean makeReadable() {
        return false;
    }

    @Override
    protected JsonWriter newWriter(String path) throws IOException {
        JsonWriter result = new JsonWriter(new FileWriter(fileAt(path, true)));
        if (makeReadable()) {
            result.setIndent("  ");
        }
        return result;
    }

    @Override
    protected void write(T object, JsonWriter writerTo) throws IOException {
        object.writeTo(writerTo);
    }

    @Override
    protected JsonReader newReader(String path) throws IOException {
        return new JsonReader(new FileReader(fileAt(path, false)));
    }

    @Override
    protected T read(JsonReader readerFrom) throws IOException {
        T result = createNew();
        result.loadFrom(readerFrom);
        return result;
    }

}
