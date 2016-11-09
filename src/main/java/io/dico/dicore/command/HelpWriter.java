package io.dico.dicore.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpWriter {
	
	private static final int PAGE_SIZE = 8; // + 2 for header and footer
	
	private static final String 
	 	HEADER_FORMAT 			= Formatting.translateChars('&', "&6Help page %s about &3/%s&6,"),
	 	HELP_INFORMATION_FORMAT	= Formatting.translateChars('&', "  &6%s"),
	 	ALIASES_FORMAT 			= Formatting.translateChars('&', "&6Aliases: &3/%s"),
	 	SYNTAX_FORMAT 			= Formatting.translateChars('&', "&6Syntax: &3/%s %s"),
	 	SUBCOMMAND_HEADER 		= Formatting.translateChars('&', "&aFollow-ups:"),
	 	SUBCOMMAND_FORMAT		= Formatting.translateChars('&', "&3/%s%s\n  &a%s"),
		FOOTER_FORMAT			= Formatting.translateChars('&', "\n&6Use &3/%s help %s &6for the next page"),
		NOT_A_PAGE_FORMAT		= HEADER_FORMAT + " does not exist";
	
	private String[] helpMessage;
	private final String command, syntaxMessage;
	private final Command handler;
	
	public HelpWriter(Command handler, String command, String[] helpInformation, List<String> aliases, String syntax) {
		List<String> lines = new ArrayList<>();
		
		Arrays.stream(helpInformation).map(s -> String.format(HELP_INFORMATION_FORMAT, s)).forEach(lines::add);
		
		if (aliases.size() > 0)
			lines.add(String.format(ALIASES_FORMAT, String.join(Formatting.translateChars('&', "&a, &3/"), aliases)));
		
		this.syntaxMessage = String.format(SYNTAX_FORMAT, command, syntax);
		
		if (syntax != null && !syntax.isEmpty())
			lines.add(syntaxMessage);

		this.helpMessage = lines.toArray(new String[lines.size()]);
		this.command = command;
		this.handler = handler;
	}
	
	public String parseHelpMessage(CommandSender sender, int page) {
		if (page > 1000) {
			return String.format(NOT_A_PAGE_FORMAT, page, command);
		}
		
		StringBuilder message = new StringBuilder(String.format(HEADER_FORMAT, page, command));
		
		int end = page * PAGE_SIZE;
		int start = end - PAGE_SIZE;
		String[] helpToDisplay = Arrays.copyOfRange(helpMessage, Math.min(start, helpMessage.length), Math.min(end, helpMessage.length));
		
		int cmdsDisplayed = 0;
		for (int i = 1; i < page; i++) {
			cmdsDisplayed += getCommandsDisplayed(i);
		}
		int onThisPage = getCommandsDisplayed(page);
		
		Command[] cmds = handler.getChildren().stream().filter(child -> child.accepts(sender)).toArray(size -> new Command[size]);
		int layer = handler.getLayer();
		String[] cmdsToDisplay = Arrays.stream(Arrays.copyOfRange(cmds, Math.min(cmdsDisplayed, cmds.length), Math.min(cmdsDisplayed + onThisPage, cmds.length)))
				.map(c -> String.format(SUBCOMMAND_FORMAT, command, c.collectPath(layer), c.getDescription())).toArray(size -> new String[size]);
		
		if (helpToDisplay.length == 0 && cmdsToDisplay.length == 0) {
			return String.format(NOT_A_PAGE_FORMAT, page, command);
		}
		
		for (String s : helpToDisplay)
			message.append('\n' + s);
		for (String s : cmdsToDisplay)
			message.append('\n' + s);
		
		message.append(String.format(FOOTER_FORMAT, command, page + 1));
		
		return message.toString();
	}
	
	private int getCommandsDisplayed(int page) {
		int index = page * PAGE_SIZE;
		return (PAGE_SIZE - (Math.min(index, helpMessage.length) - Math.min(index - PAGE_SIZE, helpMessage.length))) / 2;
	}
	
	public String getSyntaxMessage() {
		return syntaxMessage;
	}
	
	void addSubcommandHeader() {
		if (helpMessage.length == 0 || !helpMessage[helpMessage.length - 1].equals(SUBCOMMAND_HEADER)) {
			helpMessage = Arrays.copyOfRange(helpMessage, 0, helpMessage.length + 1);
			helpMessage[helpMessage.length - 1] = SUBCOMMAND_HEADER;
		}
	}
}
