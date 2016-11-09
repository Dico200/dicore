package io.dico.dicore.saving;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.function.Supplier;

public class JsonLoadableAdapter extends TypeAdapter<JsonLoadable> {

    private final Supplier<JsonLoadable> constructor;

    public JsonLoadableAdapter(Supplier<JsonLoadable> constructor) {
        this.constructor = constructor;
    }

    @Override
    public void write(JsonWriter writer, JsonLoadable loadable) throws IOException {
        loadable.writeTo(writer);
    }

    @Override
    public JsonLoadable read(JsonReader reader) throws IOException {
        JsonLoadable loadable = constructor.get();
        loadable.loadFrom(reader);
        return loadable;
    }

}
