package io.dico.dicore.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class Validate {
	
	public static void isTrue(boolean expression, String failMessage) {
		if (!expression) {
			throw new CommandException(failMessage);
		}
	}
	
	public static void notNull(Object obj, String failMessage) {
		if (obj == null) {
			throw new CommandException(failMessage);
		}
	}
	
	public static void isAuthorized(CommandSender sender, String permission, String failMessage) {
		if (!sender.hasPermission(permission)) {
			throw new CommandException(failMessage);
		}
	}
	
	public static void isAuthorized(CommandSender sender, String permission) {
		Validate.isAuthorized(sender, permission, "You do not have permission to use that command");
	}
	
	public static void isPlayer(CommandSender sender) {
		if (!(sender instanceof Player)) {
			throw new CommandException("That command can only be used by players");
		}
	}
	
	public static void isConsole(CommandSender sender) {
		if (!(sender instanceof ConsoleCommandSender)) {
			throw new CommandException("That command can only be used by the console");
		}
	}
	
	public static <T> T returnIfPresent(Optional<T> maybe, String failMessage) {
		return maybe.orElseThrow(() -> new CommandException(failMessage));
	}
	
	private Validate() {
		
	}

}
