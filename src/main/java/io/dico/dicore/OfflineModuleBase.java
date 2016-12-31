package io.dico.dicore;

import io.dico.dicore.saving.Saveable;
import io.dico.dicore.saving.fileadapter.FileAdapter;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

abstract class OfflineModuleBase<P extends Plugin, T> extends Module<P> implements Saveable {

    private T data;
    private final FileAdapter<T, ?, ?, ?> fileAdapter;
    private final String file;
    private boolean saveScheduled = false;

    public OfflineModuleBase(String name, P plugin, boolean usesConfig, boolean debugging) {
        super(name, plugin, usesConfig, debugging);
        file = new File(getDataFolder(), "data.json").getAbsolutePath();

        final Consumer<Throwable> onErrorLoad = t -> {
            error("Error occurred whilst loading data for module " + getName());
            t.printStackTrace();
        };

        final Consumer<Throwable> onErrorSave = t -> {
            error("Failed to save data for module " + getName());
            t.printStackTrace();
        };

        fileAdapter = Objects.requireNonNull(newAdapter(onErrorLoad, onErrorSave));
    }

    abstract FileAdapter<T, ?, ?, ?> newAdapter(Consumer<Throwable> onErrorLoad, Consumer<Throwable> onErrorSave);

    protected abstract T generateDefaultData();

    protected void onDataUpdate() {
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
