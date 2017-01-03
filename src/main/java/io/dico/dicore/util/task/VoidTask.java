package io.dico.dicore.util.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class VoidTask {
    private boolean running = false;
    private Integer taskId = null;
    private long workTime;

    public void start(Plugin plugin, int delay, int period, long workTime) {
        if (running) {
            throw new IllegalStateException("already running");
        }
        this.workTime = workTime;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::run, delay, period);
        running = true;
    }

    public void start(Plugin plugin) {
        start(plugin, 2, 2, 20);
    }

    public boolean isRunning() {
        return running;
    }

    protected void onFinish(Exception thrown) {
    }

    private void cancelTask(Exception thrown) {
        Bukkit.getScheduler().cancelTask(taskId);
        running = false;
        taskId = null;
        onFinish(thrown);
    }

    private void tick() {
        final long stop = System.currentTimeMillis() + workTime;
        do {
            try {
                if (!run()) {
                    cancelTask(null);
                    return;
                }
            } catch (Exception ex) {
                cancelTask(ex);
                return;
            }
        } while (System.currentTimeMillis() < stop);
    }

    protected abstract boolean run();

}
