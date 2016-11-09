package io.dico.dicore.saving;

public class SubSaveable implements Saveable {

    private final Saveable superSaveable;

    public SubSaveable(Saveable superSaveable) {
        this.superSaveable = superSaveable;
    }

    @Override
    public void scheduleSave() {
        superSaveable.scheduleSave();
    }

    @Override
    public boolean isSaveScheduled() {
        return false;
    }
}
