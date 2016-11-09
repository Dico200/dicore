package io.dico.dicore.util.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class BaseTask<T> implements Runnable {

    private boolean running = false;

    private Integer taskId = null;
    private long workTime = 20;

    public void start(Plugin plugin, long delay, long period, long workTime) {
        this.workTime = workTime;
        running = true;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, period);
    }

    public void start(Plugin plugin) {
        start(plugin, 2, 2, 20);
    }

    protected void onFinish(boolean early) {
    }

    protected abstract boolean process(T object);

    @Override
    public void run() {
        if (taskId == null) return;
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < workTime && processNext()) ;
    }

    public boolean isRunning() {
        return running;
    }

    protected abstract T supply();

    private void cancelTask(boolean early) {
        Bukkit.getScheduler().cancelTask(taskId);
        onFinish(early);
        running = false;
    }

    private boolean processNext() {
        final T object = supply();
        if (object == null) {
            cancelTask(false);
            return false;
        }

        if (!process(object)) {
            cancelTask(true);
            return false;
        }
        return true;
    }

}
