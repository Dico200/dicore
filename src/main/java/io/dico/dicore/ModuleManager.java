package io.dico.dicore;

import io.dico.dicore.command.Formatting;
import io.dico.dicore.util.Logging;
import io.dico.dicore.util.Registrator;
import io.dico.dicore.util.TickTask;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Set;

public interface ModuleManager extends Logging {

    Set<Module> getModules();

    default <T extends Module> T getModule(String name) {
        for (Module module : getModules()) {
            if (name.equals(module.getName())) {
                try {
                    return (T) module;
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }
        return null;
    }

    boolean isDebugging();

    void setDebugging(boolean debugging);

    Registrator getRegistrator();

    void registerModule(Class<? extends Module> clazz);

    void registerModule(Module module);
    
    void registerModule(String name, Module module);

    TickTask getTickTask();

    TickTask getModuleTickTask();

    String getMessagePrefix();

    void setMessagePrefix(String prefix);

    default void sendMessage(CommandSender playerTo, String message, Object... args) {
        playerTo.sendMessage(getMessagePrefix() + Formatting.translateChars('&', String.format(message, args)));
    }

    boolean isEnabled();

    Plugin getPlugin();

    Server getServer();

    String getName();

    File getDataFolder();

}
