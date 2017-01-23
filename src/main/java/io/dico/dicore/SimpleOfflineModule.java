package io.dico.dicore;

import io.dico.dicore.saving.JsonLoadable;
import io.dico.dicore.saving.fileadapter.FileAdapter;
import io.dico.dicore.saving.fileadapter.JsonFileAdapter;

import java.util.function.Consumer;

public abstract class SimpleOfflineModule<Manager extends ModuleManager, Data extends JsonLoadable> extends OfflineModuleBase<Manager, Data> {
    
    protected SimpleOfflineModule(String name, Manager manager, boolean usesConfig, boolean debugging) {
        super(name, manager, usesConfig, debugging);
    }
    
    @Override
    FileAdapter<Data> newAdapter(Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave) {
        return JsonFileAdapter.create(this::generateDefaultData, onErrorLoad, onErrorSave);
    }
    
}
