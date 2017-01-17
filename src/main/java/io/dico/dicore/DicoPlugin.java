package io.dico.dicore;

import io.dico.dicore.command.Formatting;
import io.dico.dicore.util.Modules;
import io.dico.dicore.util.Registrator;
import io.dico.dicore.util.TickTask;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DicoPlugin extends JavaPlugin implements ModuleManager {
    private final Registrator registrator;
    private final Set<Module> modules = new HashSet<>();
    private TickTask tickTask;
    private TickTask moduleTickTask;
    private String messagePrefix;
    private boolean debugging;

    public DicoPlugin() {
        registrator = new Registrator(this);
        messagePrefix = Formatting.translateChars('&', "&4[&c" + getName() + "&4] &a");
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
                Bukkit.getPluginManager().registerEvents((Listener) module, this);
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
            tickTask = new TickTask(this) {
                @Override
                protected void tick() {
                    DicoPlugin.this.tick();
                }
            };
        }
        return tickTask;
    }

    @Override
    public TickTask getModuleTickTask() {
        if (moduleTickTask == null) {
            moduleTickTask = new TickTask(this) {
                @Override
                protected void tick() {
                    DicoPlugin.this.tickModules();
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
    public void error(Object o) {
        getLogger().severe(String.valueOf(o));
    }

    @Override
    public void info(Object o) {
        getLogger().info(String.valueOf(o));
    }

    @Override
    public void debug(Object o) {
        if (debugging) {
            getLogger().info(String.format("[DEBUG] %s", String.valueOf(o)));
        }
    }

    @Override
    public boolean isDebugging() {
        return debugging;
    }

    @Override
    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
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
    public final void onEnable() {
        if (!preEnable()) {
            getLogger().severe("An error occurred whilst enabling plugin " + getName() +" !");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        enable();

        if (this instanceof Listener) {
            getServer().getPluginManager().registerEvents((Listener) this, this);
        }
    }

    @Override
    public final void onDisable() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.setEnabled(false);
            }
        }
        disable();
    }

    @Override
    public Plugin getPlugin() {
        return this;
    }

}
