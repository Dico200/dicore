package io.dico.dicore;

import io.dico.dicore.saving.JsonLoadable;
import io.dico.dicore.saving.fileadapter.FileAdapter;
import io.dico.dicore.saving.fileadapter.JsonFileAdapter;

import java.util.function.Consumer;

public abstract class SimpleOfflineModule<P extends DicoPlugin, T extends JsonLoadable> extends OfflineModuleBase<P, T> {

    public SimpleOfflineModule(String name, P plugin, boolean usesConfig, boolean debugging) {
        super(name, plugin, usesConfig, debugging);
    }

    @Override
    FileAdapter<T> newAdapter(Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave) {
        return JsonFileAdapter.create(this::generateDefaultData, onErrorLoad, onErrorSave);
    }

}
