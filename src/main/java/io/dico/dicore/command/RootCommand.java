package io.dico.dicore.command;

import org.bukkit.command.CommandSender;

public class RootCommand extends Command {
    
    public RootCommand(String command) {
        super(command);
    }
    
    @Override
    protected String execute(CommandSender sender, CommandScape scape) {
        return "EXEC:CommandAction.DISPLAY_HELP";
    }
    
}
