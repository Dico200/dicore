package io.dico.dicore.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CommandManager {

    private static final Map<String, org.bukkit.command.Command> COMMAND_MAP;
    private static final Command ROOT;
    private static String prefix;
    private static String prefixFormat;

    static Command getRoot() {
        return ROOT;
    }

    public static void register(String prefixFormat, String prefix, Command handler) {
        CommandManager.prefixFormat = prefixFormat;
        CommandManager.prefix = prefix;
        ROOT.addChild(handler);
    }

    public static void register(String prefix, Command handler) {
        register(Messaging.PREFIX_FORMAT, prefix, handler);
    }

    public static void register(Command handler) {
        register(null, handler);
    }

    private static void dispatchToMap(Command command) {
        assert COMMAND_MAP != null : "Command Map wasn't retrieved, unable to register commands!";
        assert command != null : "Dispatched command is null!";

        InputHandler handler = new InputHandler(command, prefixFormat, prefix);
        String id = command.getId();

        org.bukkit.command.Command other = COMMAND_MAP.put(id, handler);
        for (String alias : command.getAliases()) {
            org.bukkit.command.Command overridden = COMMAND_MAP.put(alias, handler);
            if (other == null) {
                other = overridden;
            }
        }
        if (other != null) {
            handler.setOther(other);
        }
    }

    static {

        PluginManager pm = Bukkit.getPluginManager();
        Map<String, org.bukkit.command.Command> map2;
        try {
            Field f = pm.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap commandMap = (CommandMap) f.get(pm);

            Field f2 = commandMap.getClass().getDeclaredField("knownCommands");
            f2.setAccessible(true);
            map2 = (Map<String, org.bukkit.command.Command>) f2.get(commandMap);

        } catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException | NullPointerException | ClassCastException e) {
            Bukkit.getLogger().severe("An error occured while retrieving the command map! See below.");
            e.printStackTrace();
            map2 = null;
        }

        COMMAND_MAP = map2;

        ROOT = new Command() {

            @Override
            protected String execute(CommandSender sender, CommandScape scape) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected List<String> tabComplete(CommandSender sender, CommandScape scape) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected boolean addChild(String key, Hierarchy<Command> child) {
                if (super.addChild(key, child)) {
                    dispatchToMap(child.getInstance());
                    return true;
                }
                return false;
            }

        };

    }

}
