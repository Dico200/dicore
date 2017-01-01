package io.dico.dicore.util.task;

import org.bukkit.plugin.Plugin;

import java.util.Iterator;

public abstract class StagingIteratorTask<T> extends IteratorTask<T> {
    private int stage;
    private Plugin plugin;
    private int period;
    private long workTime;

    @Override
    public void start(Plugin plugin, int delay, int period, long workTime) {
        if (isRunning()) {
            throw new IllegalStateException("Can't start when already running");
        }
        this.plugin = plugin;
        this.period = period;
        this.workTime = workTime;
        super.start(plugin, delay, period, workTime);
    }

    protected int currentStage() {
        return stage;
    }

    public StagingIteratorTask(int stage) {
        refresh(newIterator(stage));
    }

    protected abstract Iterator<? extends T> newIterator(int stage);

    protected abstract int stageOf(T instance);

    @Override
    protected final boolean process(T object) {
        if (stageOf(object) == stage) {
            process(object, stage);
        }
        return true;
    }

    protected abstract boolean process(T object, int stage);

    protected abstract int nextStage(int stage);

    @Override
    protected final void onFinish(boolean early) {
        int nextStage = nextStage(stage);
        if (nextStage == -1) {
            onFinish(early);
        } else {
            stage = nextStage;
            refresh(newIterator(stage));
            start(plugin, period, period, workTime);
        }
    }

    protected void finished(boolean early) {
    }

}
