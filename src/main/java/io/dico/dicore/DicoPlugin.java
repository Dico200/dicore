package io.dico.dicore;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import io.dico.dicore.command.Formatting;
import io.dico.dicore.saving.JsonFileAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class DicoPlugin extends JavaPlugin implements Logging {

    private String messagePrefix = Formatting.translateChars('&', "&c[&4" + getName() + "&c]&a ");
    private final Set<Module> modules = new HashSet<>();
    private BukkitTask tickTask;
    private BukkitTask moduleTickTask;
    private boolean debugging = false;

    // ----- LOGGING -----

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

    public boolean isDebugging() {
        return debugging;
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }

    // ----- MESSAGES -----

    public String getMessagePrefix() {
        return messagePrefix;
    }

    public void setMessagePrefix(String messagePrefix) {
        this.messagePrefix = Formatting.translateChars('&', messagePrefix);
    }

    public void sendMessage(CommandSender playerTo, String message, Object... args) {
        playerTo.sendMessage(messagePrefix + Formatting.translateChars('&', String.format(message, args)));
    }

    // ----- MODULES -----

    public Set<Module> getModules() {
        return modules;
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

    // ----- FILE ADAPTERS -----

    protected <T> JsonFileAdapter<T> createFileAdapter(Type typeOfT, GsonBuilder gson) {
        return new JsonFileAdapter<>(typeOfT, gson.setPrettyPrinting().create(), this::error, this::error);
    }

    protected <T> JsonFileAdapter<T> createFileAdapter(Type typeOfT, TypeAdapter<? super T> typeTAdapter) {
        return createFileAdapter(typeOfT, new GsonBuilder().registerTypeAdapter(typeOfT, typeTAdapter));
    }

    // ----- TICK TASKS -----

    protected void stopTicking() {
        tickTask.cancel();
        tickTask = null;
    }

    protected BukkitTask getTickTask() {
        return tickTask;
    }

    protected BukkitTask getModuleTickTask() {
        return moduleTickTask;
    }

    protected void stopTickingModules() {
        moduleTickTask.cancel();
        moduleTickTask = null;
    }

    protected boolean isTickingModules() {
        return isRunning(moduleTickTask);
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

    // ----- STATE CHANGES -----

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

    protected boolean preEnable() {
        return true;
    }

    protected void enable() {
    }

    protected void disable() {
    }
}
