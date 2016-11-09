package io.dico.dicore;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class DicoPlugin extends JavaPlugin implements Logging {

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
        if (isDebugging()) {
            getLogger().info("[DEBUG]" + String.valueOf(o));
        }
    }

    protected boolean isDebugging() {
        return true;
    }

    private final Set<Module> modules = new HashSet<>();
    private BukkitTask tickTask;
    private BukkitTask moduleTickTask;

    protected void registerModule(Module module) {
        try {
            module.setEnabled(true);
            if (module instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) module, this);
            }
        } catch (Throwable t) {
            error("Failed to enable module " + module.getName());
            t.printStackTrace();
            return;
        }
        modules.add(module);
    }

    protected void registerModuleClass(Class<? extends Module> moduleClass) {
        try {
            Module module = moduleClass.newInstance();
            registerModule(module);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T extends Module> T getModule(String name) {
        for (Module module : modules) {
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

    protected void startTickingModules(int delay, int period) {
        if (moduleTickTask != null) {
            moduleTickTask.cancel();
        }
        moduleTickTask = getServer().getScheduler().runTaskTimer(this, this::tickModules, delay, period);
    }

    protected boolean isTickingModules() {
        return isRunning(moduleTickTask);
    }

    protected void startTicking(int delay, int period) {
        if (tickTask != null) {
            tickTask.cancel();
        }
        tickTask = getServer().getScheduler().runTaskTimer(this, this::tick, delay, period);
    }

    protected boolean isTicking() {
        return isRunning(tickTask);
    }

    protected void tick() {
    }

    private boolean isRunning(BukkitTask task) {
        return task != null && getServer().getScheduler().isCurrentlyRunning(task.getTaskId());
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
    public final void onEnable() {
        enable();
    }

    protected void enable() {
    }

    protected void disable() {
    }
}
