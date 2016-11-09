package io.dico.dicore.saving;

public abstract class SaveableImpl implements Saveable {

    private boolean isSaveScheduled = false;

    @Override
    public void scheduleSave() {
        isSaveScheduled = true;
    }

    @Override
    public boolean isSaveScheduled() {
        if (isSaveScheduled) {
            isSaveScheduled = false;
            return true;
        }
        return false;
    }
}
