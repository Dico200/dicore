package io.dico.dicore.util.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.NoSuchElementException;

public abstract class BaseTask<T> {
    private boolean running = false;
    private Integer taskId = null;
    private long workTime;

    public void start(Plugin plugin, int delay, int period, long workTime) {
        this.workTime = workTime;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::run, delay, period);
        running = true;
    }

    public void start(Plugin plugin) {
        start(plugin, 2, 2, 20);
    }

    protected void onFinish(boolean early) {
    }

    protected long getWorkTime() {
        return workTime;
    }

    protected abstract boolean process(T object);

    private void run() {
        final long stop = System.currentTimeMillis() + getWorkTime();
        do {
            if (!processNext()) {
                return;
            }
        } while (System.currentTimeMillis() < stop);
    }

    public int getTaskId() {
        return running ? taskId : -1;
    }

    public boolean isRunning() {
        return running;
    }

    protected abstract T supply() throws NoSuchElementException;

    private void cancelTask(boolean early) {
        Bukkit.getScheduler().cancelTask(taskId);
        running = false;
        taskId = null;
        onFinish(early);
    }

    private boolean processNext() {
        T object;
        try {
            object = supply();
        } catch (NoSuchElementException e) {
            cancelTask(false);
            return false;
        }

        try {
            if (process(object)) {
                return true;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        cancelTask(true);
        return false;
    }

}
