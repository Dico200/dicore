package io.dico.dicore.nms.impl.v1_7_R4;

import io.dico.dicore.nms.NProperties;
import net.minecraft.server.v1_7_R4.PropertyManager;

import java.io.File;

class PropertiesImpl implements NProperties {
    private PropertyManager propertyManager;

    public PropertiesImpl(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    @Override
    public void save() {
        propertyManager.savePropertiesFile();
    }

    @Override
    public File getFile() {
        return propertyManager.c();
    }

    @Override
    public String getString(String key, String absent) {
        return propertyManager.getString(key, absent);
    }

    @Override
    public int getInt(String key, int absent) {
        return propertyManager.getInt(key, absent);
    }

    @Override
    public long getLong(String key, long absent) {
        Object o = propertyManager.properties.get(key);
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        return absent;
    }

    @Override
    public boolean getBoolean(String key, boolean absent) {
        return propertyManager.getBoolean(key, absent);
    }

    @Override
    public void setProperty(String key, Object value) {
        propertyManager.setProperty(key, value);
    }
}
