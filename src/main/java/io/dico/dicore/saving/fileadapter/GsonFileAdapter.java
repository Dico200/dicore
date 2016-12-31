package io.dico.dicore.saving.fileadapter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public abstract class GsonFileAdapter<T> extends FileAdapter<T, FileWriter, FileReader, JsonParseException> {

    private final Type typeOfT;
    private final Gson gson;

    public GsonFileAdapter(Type typeOfT, Gson gson) {
        this.typeOfT = typeOfT;
        this.gson = gson;
    }

    @Override
    protected FileWriter newWriter(String path) throws JsonParseException {
        try {
            return new FileWriter(fileAt(path, true));
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    protected void write(T object, FileWriter writerTo) throws JsonParseException {
        gson.toJson(object, writerTo);
    }

    @Override
    protected FileReader newReader(String path) throws JsonParseException {
        try {
            return new FileReader(fileAt(path, false));
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    protected T read(FileReader readerFrom) throws JsonParseException {
        return gson.fromJson(readerFrom, typeOfT);
    }

}
