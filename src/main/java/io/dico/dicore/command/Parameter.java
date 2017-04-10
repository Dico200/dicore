package io.dico.dicore.command;

import java.util.List;

public class Parameter<T> {
    
    private String name;
    private ParameterType<T> type;
    private String description;
    private boolean required;
    private int index;
    private boolean hasDefault;
    private T defaultValue;
    
    public Parameter(String name, ParameterType<T> type, String description, boolean required) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.required = required;
        this.index = 0;
        hasDefault = false;
    }
    
    public Parameter(String name, ParameterType<T> type, String description) {
        this(name, type, description, true);
    }
    
    public Parameter(String name, ParameterType<T> type, String description, T defaultValue) {
        this(name, type, description, true);
        hasDefault = true;
        this.defaultValue = defaultValue;
    }
    
    String getName() {
        return name;
    }
    
    ParameterType<T> getHandler() {
        return type;
    }
    
    boolean isRequired() {
        return required;
    }
    
    int getIndex() {
        return index;
    }
    
    void setIndex(int index) {
        this.index = index;
    }
    
    public T accept(String input) {
        if (input == null || input.isEmpty()) {
            if (hasDefault)
                return defaultValue;
            if (required)
                throw new CommandException("EXEC:CommandAction.DISPLAY_SYNTAX");
            return null;
        }
        try {
            return type.handle(input);
        } catch (CommandException e) {
            throw new CommandException(e.getMessage().replace("$ARG$", name).replace("$DESC$", description));
        }
    }
    
    public boolean isHasDefault() {
        return hasDefault;
    }
    
    public T getDefaultValue() {
        return defaultValue;
    }
    
    public List<String> complete(String input) {
        return type.complete(input);
    }
    
    public String syntax() {
        String syntax = name.trim();
        return String.format(required ? "<%s>" : "[%s]", syntax);
    }
    
}
