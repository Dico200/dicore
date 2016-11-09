package io.dico.dicore.command;

import org.bukkit.command.CommandSender;

public class Messaging {
	
	static void print(String s) {
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		print(Formatting.translateChars('&', "&4&c&f&h"));
	}

	public static final Formatting SUCCESS = Formatting.GREEN;
	public static final Formatting EXCEPT = Formatting.YELLOW;
	
	private static final String PREFIX_FORMAT = Formatting.translateChars('&', "&4[&c%s&4] ");
	private static final String MESSAGE_FORMAT = "%s%s%s";
	
	private static String formatPrefix(String prefix) {
		return (prefix == null || prefix.isEmpty())? "" : String.format(PREFIX_FORMAT, prefix);
	}

	public static void send(CommandSender recipient, String prefix, Formatting format, String message) {
		recipient.sendMessage(String.format(MESSAGE_FORMAT, formatPrefix(prefix), format, Formatting.translateChars('&', message)));
	}
	
	public static void send(CommandSender recipient, Formatting format, String message) {
		send(recipient, null, format, message);
	}
	
}

