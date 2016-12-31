package io.dico.dicore;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.JsonLoadable;
import io.dico.dicore.saving.fileadapter.FileAdapter;
import io.dico.dicore.saving.fileadapter.JsonFileAdapter;

import java.io.IOException;
import java.util.function.Consumer;

public abstract class SimpleOfflineModule<P extends DicoPlugin, T extends JsonLoadable> extends OfflineModuleBase<P, T> {

    public SimpleOfflineModule(String name, P plugin, boolean usesConfig, boolean debugging) {
        super(name, plugin, usesConfig, debugging);
    }

    @Override
    FileAdapter<T, JsonWriter, JsonReader, IOException> newAdapter(Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave) {
        return new JsonFileAdapter<T>() {
            @Override
            protected T createNew() {
                return generateDefaultData();
            }

            @Override
            protected void onErrorLoad(Exception ex) {
                onErrorLoad.accept(ex);
            }

            @Override
            protected void onErrorSave(Exception ex) {
                onErrorSave.accept(ex);
            }
        };
    }

}
