package io.dico.dicore.commandx.parameter;

public class IndexedParameter {

    private final String name;
    private final String defaultValue;

    public IndexedParameter(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return defaultValue != null;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
