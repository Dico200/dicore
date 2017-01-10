package io.dico.dicore.nms.impl.unknown;

import io.dico.dicore.nms.NProperties;
import org.bukkit.Bukkit;

import java.io.File;

class Properties_UNKNOWN implements NProperties {

    @Override
    public void save() {

    }

    @Override
    public File getFile() {
        return new File(Bukkit.getServer().getUpdateFolderFile(), "server.properties");
    }

    @Override
    public String getString(String key, String absent) {
        return absent;
    }

    @Override
    public int getInt(String key, int absent) {
        return absent;
    }

    @Override
    public long getLong(String key, long absent) {
        return absent;
    }

    @Override
    public boolean getBoolean(String key, boolean absent) {
        return absent;
    }

    @Override
    public void setProperty(String key, Object value) {

    }
}
