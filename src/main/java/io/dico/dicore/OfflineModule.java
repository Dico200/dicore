package io.dico.dicore;

import com.google.gson.Gson;
import io.dico.dicore.saving.fileadapter.FileAdapter;
import io.dico.dicore.saving.fileadapter.GsonFileAdapter;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public abstract class OfflineModule<P extends Plugin, T> extends OfflineModuleBase<P, T> {
    private final Type typeOfT = getDataType();

    public OfflineModule(String name, P plugin, boolean usesConfig, boolean debugging) {
        super(name, plugin, usesConfig, debugging);
    }

    protected Type getTypeOfT() {
        return typeOfT;
    }

    protected abstract Gson createGson();

    protected abstract Type getDataType();

    @Override
    FileAdapter<T> newAdapter(Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave) {
        return GsonFileAdapter.create(typeOfT, createGson(), onErrorLoad, onErrorSave);
    }

}
