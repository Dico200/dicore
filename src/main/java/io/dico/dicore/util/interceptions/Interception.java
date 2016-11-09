package io.dico.dicore.util.interceptions;

import io.dico.dicore.command.CommandException;
import io.dico.dicore.command.Formatting;
import io.dico.dicore.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interception extends Command {

    public static boolean isCommandMapAvailable() {
        return commandMap != null;
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

        new HashMap<>(commandMap).forEach((name, cmd) -> {
            if (cmd == intercepted) {
                commandMap.put(name, interception);
            }
        });
        return interception;
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
    public Command setPermissionMessage(String permissionMessage) {
        return super.setPermissionMessage(permissionMessage);
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

    static {
        commandMap = (Map<String, Command>) Reflection.getValueInField(Reflection.getValueInField(
                Bukkit.getPluginManager(), "commandMap"), "knownCommands");
    }

}
