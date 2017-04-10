package io.dico.dicore.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public enum SenderType {
    
    PLAYER(Player.class, "That command can only be used by players"),
    CONSOLE(ConsoleCommandSender.class, "That command can only be used by the console"),
    EITHER(CommandSender.class, null);
    
    private final Class<?> type;
    private final String message;
    
    private SenderType(Class<?> type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public boolean correct(CommandSender sender) {
        return type.isInstance(sender);
    }
    
    public void check(CommandSender sender) {
        Validate.isTrue(correct(sender), message);
    }
    
}
