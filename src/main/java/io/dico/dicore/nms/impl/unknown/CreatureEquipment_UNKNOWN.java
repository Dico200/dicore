package io.dico.dicore.nms.impl.unknown;

import io.dico.dicore.nms.NCreatureEquipment;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;

class CreatureEquipment_UNKNOWN implements NCreatureEquipment {
    private final Creature creature;

    public CreatureEquipment_UNKNOWN(Creature creature) {
        this.creature = creature;
    }

    @Override
    public Creature getCreature() {
        return creature;
    }

    @Override
    public ItemStack getItem(int index) {
        return null;
    }

    @Override
    public void setItem(int index, ItemStack item) {

    }

    @Override
    public void refreshCache() {

    }
}
