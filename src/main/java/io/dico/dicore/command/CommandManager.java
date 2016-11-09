package io.dico.dicore.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

public class CommandManager {
	
	private static final CommandMap COMMAND_MAP;
	private static final Command ROOT;
	private static String prefix;
	
	static Command getRoot() {
		return ROOT;
	}
	
	public static void register(String prefix, Command handler) {
		CommandManager.prefix = prefix;
		ROOT.addChild(handler);
	}
	
	public static void register(Command handler) {
		register(null, handler);
	}
	
	private static void dispatchToMap(Command command) {
		assert COMMAND_MAP != null : new AssertionError("Command Map wasn't retrieved, unable to register commands!");
		assert command != null : new AssertionError("Dispatched command is null!");

		InputHandler handler = new InputHandler(command, prefix);
		String id = command.getId();

		handler.setOther(COMMAND_MAP.getCommand(id));
		COMMAND_MAP.register(id, handler);
	}
	
	static {
		
		PluginManager pm = Bukkit.getPluginManager();
		CommandMap map;
		try {
			Field f = pm.getClass().getDeclaredField("commandMap");
			f.setAccessible(true);
			map = (CommandMap) f.get(pm);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException e) {
			Bukkit.getLogger().severe("An error occured while retrieving the command map! See below.");
			e.printStackTrace();
			map = null;
		}
		
		COMMAND_MAP = map;
		
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
