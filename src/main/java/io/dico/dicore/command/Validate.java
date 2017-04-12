package io.dico.dicore.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import java.util.Optional;

public class Validate {
    
    //@Contract("false, _ -> fail")
    public static void isTrue(boolean expression, String failMessage) {
        if (!expression) {
            throw new CommandException(failMessage);
        }
    }
    
    //@Contract("null, _ -> fail")
    public static void notNull(Object obj, String failMessage) {
        Validate.isTrue(obj != null, failMessage);
    }
    
    public static void isAuthorized(CommandSender sender, String permission, String failMessage) {
        Validate.isTrue(sender.hasPermission(permission), failMessage);
    }
    
    public static void isAuthorized(CommandSender sender, String permission) {
        Validate.isAuthorized(sender, permission, "You do not have permission to use that command");
    }
    
    //@Contract("null -> fail")
    public static void isPlayer(CommandSender sender) {
        isTrue(sender instanceof Player, "That command can only be used by players");
    }
    
    //@Contract("null -> fail")
    public static void isConsole(CommandSender sender) {
        isTrue(sender instanceof ConsoleCommandSender, "That command can only be used by the console");
    }
    
    public static <T> T returnIfPresent(Optional<T> maybe, String failMessage) {
        if (!maybe.isPresent()) {
            throw new CommandException(failMessage);
        }
        return maybe.get();
    }
    
    private Validate() {
        throw new UnsupportedOperationException();
    }
    
}
