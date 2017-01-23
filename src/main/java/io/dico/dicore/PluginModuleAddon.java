package io.dico.dicore;

import io.dico.dicore.command.Formatting;
import io.dico.dicore.util.Logging;
import io.dico.dicore.util.Modules;
import io.dico.dicore.util.Registrator;
import io.dico.dicore.util.TickTask;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PluginModuleAddon extends Logging.RootLogging implements ModuleManager {
    private final Plugin plugin;
    private final String name;
    private final Registrator registrator;
    private final Set<Module> modules = new HashSet<>();
    private TickTask tickTask;
    private TickTask moduleTickTask;
    private String messagePrefix;
    private boolean enabled;
    
    public PluginModuleAddon(Plugin plugin, String name) {
        super(Objects.requireNonNull(name), plugin.getLogger(), false);
        this.plugin = plugin;
        this.name = name;
        this.registrator = (plugin instanceof DicoPlugin) ? ((DicoPlugin) plugin).getRegistrator() : new Registrator(plugin);
        messagePrefix = Formatting.translateChars('&', "&4[&c" + plugin.getName() + "&4] &a");
    }
    
    @Override
    public Set<Module> getModules() {
        return Collections.unmodifiableSet(modules);
    }
    
    @Override
    public Registrator getRegistrator() {
        return registrator;
    }
    
    @Override
    public void registerModule(Class<? extends Module> clazz) {
        Module module = Modules.newInstanceOf(clazz, this);
        registerModule(clazz.getSimpleName(), module);
    }
    
    @Override
    public void registerModule(Module module) {
        registerModule(module.getName(), module);
    }
    
    @Override
    public void registerModule(String name, Module module) {
        try {
            module.setEnabled(true);
            if (module instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) module, plugin);
            }
        } catch (Throwable t) {
            error("Failed to enable module " + name);
            t.printStackTrace();
            return;
        }
        modules.add(module);
    }
    
    @Override
    public TickTask getTickTask() {
        if (tickTask == null) {
            tickTask = new TickTask(plugin) {
                @Override
                protected void tick() {
                    PluginModuleAddon.this.tick();
                }
            };
        }
        return tickTask;
    }
    
    @Override
    public TickTask getModuleTickTask() {
        if (moduleTickTask == null) {
            moduleTickTask = new TickTask(plugin) {
                @Override
                protected void tick() {
                    PluginModuleAddon.this.tickModules();
                }
            };
        }
        return moduleTickTask;
    }
    
    protected boolean preEnable() {
        return true;
    }
    
    protected void enable() {
        
    }
    
    protected void disable() {
        
    }
    
    protected void tick() {
        
    }
    
    protected void tickModules() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                try {
                    module.update();
                } catch (Throwable t) {
                    error("Error occurred whilst ticking module " + module.getName());
                    t.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public String getMessagePrefix() {
        return messagePrefix;
    }
    
    @Override
    public void setMessagePrefix(String prefix) {
        this.messagePrefix = prefix == null ? "" : prefix;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        if (this.enabled) {
            if (!enabled) {
                for (Module module : modules) {
                    if (module.isEnabled()) {
                        module.setEnabled(false);
                    }
                }
                disable();
                this.enabled = false;
            }
        } else if (enabled) {
            if (!preEnable()) {
                error("An error occurred whilst enabling plugin " + getName() + " !");
                return;
            }
            this.enabled = true;
            enable();
            
            if (this instanceof Listener) {
                getServer().getPluginManager().registerEvents((Listener) this, plugin);
            }
        }
    }
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
    
    @Override
    public File getDataFolder() {
        return name.isEmpty() ? plugin.getDataFolder() : new File(plugin.getDataFolder(), name);
    }
    
    @Override
    public Server getServer() {
        return plugin.getServer();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
}
