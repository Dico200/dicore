package io.dico.dicore;

import com.google.gson.Gson;
import io.dico.dicore.saving.fileadapter.FileAdapter;
import io.dico.dicore.saving.fileadapter.GsonFileAdapter;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public abstract class OfflineModule<Manager extends ModuleManager, Data> extends OfflineModuleBase<Manager, Data> {
    private final Type typeOfT = getDataType();
    
    protected OfflineModule(String name, Manager manager, boolean usesConfig, boolean debugging) {
        super(name, manager, usesConfig, debugging);
    }
    
    protected Type getTypeOfT() {
        return typeOfT;
    }
    
    protected abstract Gson createGson();
    
    protected abstract Type getDataType();
    
    @Override
    FileAdapter<Data> newAdapter(Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave) {
        return GsonFileAdapter.create(typeOfT, createGson(), onErrorLoad, onErrorSave);
    }
    
}
