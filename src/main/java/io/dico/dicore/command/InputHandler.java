package io.dico.dicore.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InputHandler extends org.bukkit.command.Command {
    
    private Command parent;
    private org.bukkit.command.Command other;
    private boolean takePriority;
    private String prefix;
    
    protected InputHandler(Command parent, String prefix) {
        this(parent, Messaging.PREFIX_FORMAT, prefix);
    }
    
    protected InputHandler(Command parent, String prefixFormat, String prefix) {
        super(parent.getId(), parent.getDescription(), "", parent.getAliases());
        this.parent = parent;
        this.other = null;
        this.takePriority = true;
        
        if (prefix == null) {
            prefix = "";
        }
        if (prefixFormat != null) {
            prefix = String.format(Formatting.translateChars('&', prefixFormat), prefix);
        }
        this.prefix = prefix;
        
        setTimingsIfNecessary();
    }
    
    public void setOther(org.bukkit.command.Command other) {
        this.other = other;
    }
    
    public org.bukkit.command.Command getOther() {
        return other;
    }
    
    public Command getParent() {
        return parent;
    }
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!takePriority && other != null)
            return other.execute(sender, label, args);
        
        Command handler = parent.instanceAt(args, true);
        assert handler != null;
        args = Arrays.copyOfRange(args, handler.getLayer() - 1, args.length);
        
        String message;
        Formatting color;
        try {
            message = handler.acceptCall(sender, args);
            color = Messaging.SUCCESS;
        } catch (CommandException e) {
            message = e.getMessage();
            color = Messaging.EXCEPT;
        } catch (ConfigException e) {
            Bukkit.getLogger().severe(String.format("Command '%s' threw ConfigException: '%s'", parent.getId(), e.getMessage()));
            e.printStackTrace();
            return true;
        }
        
        if (!(message == null || message.isEmpty())) {
            Messaging.send(sender, null, prefix, color, message);
        }
        return true;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Command handler = parent.instanceAt(args, true);
        args = Arrays.copyOfRange(args, handler.getLayer() - 1, args.length);
        return handler.acceptTabComplete(sender, args);
    }
    
    private void setTimingsIfNecessary() {
        // with paper spigot, the timings are not set by super constructor but by CommandMap.register(), which is not invoked for this system
        // I use reflection so that the project does not require paper spigot to build
        try {
            Field field = getClass().getField("timings");
            if (field.get(this) != null) return;
            Class<?> clazz = Class.forName("co.aikar.timings.TimingsManager");
            Method method = clazz.getDeclaredMethod("getCommandTiming", String.class, org.bukkit.command.Command.class);
            Object timings = method.invoke(null, "", this);
            field.set(this, timings);
        } catch (Throwable ignored) {
        }
    }
}