package io.dico.dicore.util.interceptions;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface InterceptionHandler {

    boolean handle(CommandSender sender, Interception interception, String[] args);

}
