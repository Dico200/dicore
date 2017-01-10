package io.dico.dicore.util;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class TickTask {
    private final Plugin plugin;
    private BukkitTask task;

    public TickTask(Plugin plugin) {
        this.plugin = plugin;
    }

    protected abstract void tick();

    public void start(int delay, int period) {
        stop();
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::tick, delay, period);
    }

    public boolean isTicking() {
        return task != null && plugin.getServer().getScheduler().isCurrentlyRunning(task.getTaskId());
    }

    public void stop() {
        if (isTicking()) {
            task.cancel();
        }
        task = null;
    }




}
