package io.dico.dicore.saving;

public interface Saveable {

    /**
     * Schedules a save for the next tick
     */
    void scheduleSave();

    /**
     * @return true if this instance should be saved to file
     * @implNote This method should set the inner saveScheduled flag back to false.
     */
    boolean isSaveScheduled();
}
