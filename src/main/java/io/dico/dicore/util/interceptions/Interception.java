package io.dico.dicore.util.interceptions;

import io.dico.dicore.command.CommandException;
import io.dico.dicore.command.Formatting;
import io.dico.dicore.command.InputHandler;
import io.dico.dicore.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Interception extends Command {
    
    public static boolean isCommandMapAvailable() {
        return commandMap != null;
    }
    
    public static Map<String, Command> getCommandMap() {
        return commandMap;
    }
    
    public static Interception register(String prefixLabel, String commandFor, InterceptionHandler handler) {
        if (commandMap == null) {
            throw new IllegalStateException("commandMap null, can't intercept");
        }
        
        Command intercepted = commandMap.get(commandFor.toLowerCase());
        if (intercepted == null) {
            throw new IllegalArgumentException("command " + commandFor + " not found");
        }
        
        Interception interception = new Interception(prefixLabel, commandFor, intercepted, handler);
        intercept(intercepted, interception);
        return interception;
    }
    
    public static void intercept(Command present, Command replacement) {
        Iterator<Map.Entry<String, Command>> iterator = commandMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Command> next = iterator.next();
            if (next.getValue() == present) {
                if (replacement == null) {
                    iterator.remove();
                } else {
                    next.setValue(replacement);
                }
            }
        }
    }
    
    private static final Map<String, Command> commandMap;
    
    private final Command intercepted;
    private final InterceptionHandler handler;
    private final String prefixLabel;
    
    public Interception(String prefixLabel, String commandName, Command intercepted, InterceptionHandler handler) {
        super(intercepted.getName(), intercepted.getDescription(), intercepted.getUsage(), intercepted.getAliases());
        this.intercepted = intercepted;
        this.handler = handler;
        this.prefixLabel = Formatting.translateChars('&', prefixLabel);
        InputHandler.setTimingsIfNecessary(this);
    }
    
    public Interception(String prefixLabel, InterceptionHandler handler) {
        super("anonymous");
        this.prefixLabel = prefixLabel;
        intercepted = null;
        this.handler = null;
    }
    
    public Command getIntercepted() {
        return intercepted;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args);
    }
    
    @Override
    public String getPermission() {
        return super.getPermission();
    }
    
    @Override
    public void setPermission(String permission) {
        super.setPermission(permission);
    }
    
    @Override
    public boolean testPermission(CommandSender target) {
        return super.testPermission(target);
    }
    
    @Override
    public boolean testPermissionSilent(CommandSender target) {
        return super.testPermissionSilent(target);
    }
    
    @Override
    public String getLabel() {
        return super.getLabel();
    }
    
    @Override
    public boolean setLabel(String name) {
        return super.setLabel(name);
    }
    
    @Override
    public String getPermissionMessage() {
        return super.getPermissionMessage();
    }
    
    @Override
    public Interception setPermissionMessage(String permissionMessage) {
        return (Interception) super.setPermissionMessage(permissionMessage);
    }
    
    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        try {
            if (handler.handle(commandSender, this, args)) {
                return intercepted.execute(commandSender, label, args);
            }
        } catch (CommandException e) {
            send(commandSender, Formatting.translateChars('&', "&c" + e.getMessage()));
        }
        return true;
    }
    
    public void send(CommandSender sender, String message) {
        sender.sendMessage(prefixLabel + Formatting.translateChars('&', message));
    }
    
    public boolean isAnonymous() {
        return intercepted == null;
    }
    
    private void checkNotAnonymous() {
        if (isAnonymous()) {
            throw new IllegalStateException("This interception is anonymous");
        }
    }
    
    public void remove() {
        intercept(this, intercepted);
    }
    
    public void register() {
        checkNotAnonymous();
        intercept(intercepted, this);
    }
    
    public void register(Command commandFor) {
        intercept(commandFor, this);
    }
    
    static {
        commandMap = Reflection.getValueInField(Reflection.<Object>getValueInField(
                Bukkit.getPluginManager(), "commandMap"), "knownCommands");
    }
    
}
