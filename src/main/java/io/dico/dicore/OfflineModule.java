package io.dico.dicore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.dico.dicore.saving.JsonFileAdapter;
import io.dico.dicore.saving.Saveable;

import java.io.File;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public abstract class OfflineModule<P extends DicoPlugin, T> extends Module<P> implements Saveable {

    private T data;

    protected abstract Gson createGson();

    protected abstract TypeToken<T> getDataType();

    protected abstract T generateDefaultData();

    protected void onDataUpdate() {
    }

    private final JsonFileAdapter<T> fileAdapter;
    private final Type typeOfT;
    private final File file;
    private boolean saveScheduled = false;

    public OfflineModule(String name, P plugin, boolean usesConfig, boolean debugging) {
        super(name, plugin, usesConfig, debugging);
        final TypeToken<T> dataType = getDataType();
        typeOfT = dataType.getType();
        file = new File(getDataFolder(), "data.json");

        final Consumer<Throwable> onErrorLoad = t -> {
            error("Error occurred whilst loading data for module " + getName());
            t.printStackTrace();
        };

        final Consumer<Throwable> onErrorSave = t -> {
            error("Failed to save data for module " + getName());
            t.printStackTrace();
        };

        fileAdapter = new JsonFileAdapter<>(dataType, createGson(), onErrorLoad, onErrorSave);
    }

    protected Type getTypeOfT() {
        return typeOfT;
    }

    protected T getData() {
        return data;
    }

    public boolean isSaveScheduled() {
        if (saveScheduled) {
            saveScheduled = false;
            return true;
        }
        return false;
    }

    public final void scheduleSave() {
        saveScheduled = true;
    }

    private void loadData() {
        T result = fileAdapter.load(file);
        if (result == null) {
            if (data == null) {
                data = generateDefaultData();
            } else {
                return;
            }
        } else {
            data = result;
        }
        onDataUpdate();
    }

    private void saveData() {
        if (data != null) {
            fileAdapter.save(data, file);
        }
    }

    @Override
    protected void update() {
        super.update();
        if (isSaveScheduled()) {
            saveData();
        }
    }

    @Override
    void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }

        if (enabled) {
            loadData();
        } else {
            saveData();
        }
        super.setEnabled(enabled);
    }

}
