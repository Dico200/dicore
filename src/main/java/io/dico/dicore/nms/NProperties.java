package io.dico.dicore.nms;

import java.io.File;

public interface NProperties {
    void save();

    File getFile();

    String getString(String key, String absent);

    int getInt(String key, int absent);

    long getLong(String key, long absent);

    boolean getBoolean(String key, boolean absent);

    void setProperty(String key, Object value);
}
