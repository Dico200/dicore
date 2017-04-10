package io.dico.dicore.command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parameters extends LinkedList<Parameter<?>> {
    
    private static final long serialVersionUID = 7607300090766311570L;
    
    private final Command handler;
    private final boolean repeatLastParameter;
    
    public Parameters(Command handler, Parameter<?>[] params, boolean repeatLastParameter) {
        this.handler = handler;
        this.repeatLastParameter = repeatLastParameter;
        int i = 0;
        boolean lastRequired = true;
        for (Parameter<?> param : params) {
            if (!lastRequired && param.isRequired())
                throw new ConfigException("You cannot have a required parameter after one that is not required");
            lastRequired = param.isRequired();
            
            param.setIndex(i);
            this.add(param);
            i++;
        }
    }
    
    public String syntax() {
        return String.join(" ", (CharSequence[]) stream().map(param -> param.syntax()).toArray(size -> new String[size]));
    }
    
    public CommandScape toScape(String[] args) {
        return new CommandScape(this, args);
    }
    
    public CommandScape toScape(String[] args, List<String> proposals) {
        return new CommandScape(this, args, proposals);
    }
    
    public List<String> complete(String[] args) {
        int i = args.length - 1;
        return i < size() && i >= 0 ? get(i).complete(args[i]) : new ArrayList<>();
    }
    
    public boolean repeatsLastParameter() {
        return repeatLastParameter;
    }
    
    protected Command getHandler() {
        return handler;
    }
    
}
