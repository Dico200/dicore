package io.dico.dicore.nms;

import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;

public interface NCreatureEquipment {

    Creature getCreature();

    /**
     * Gets an item
     * @param index the index, between 0 and 5.
     * @return the item at this slot
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     */
    ItemStack getItem(int index);

    /**
     * Sets an item
     * @param index the index, between 0 and 5.
     * @param item the item to set at this slot
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     */
    void setItem(int index, ItemStack item);

    /**
     * Retrieved items can be stored in cache
     * To refresh said cache, call this method
     */
    void refreshCache();

    /**
     * Returns the equipment of this entity
     * Changes to entries will usually be reflected on the entity.
     * This is not a guarantee, for a guaranteed result use one of the set methods.
     * @return an ItemStack[5] of equipment
     */
    default ItemStack[] getItems() {
        ItemStack[] result = new ItemStack[5];
        for (int index = 0; index < 5; index++) {
            result[index] = getItem(index);
        }
        return result;
    }

    default ItemStack[] getArmor() {
        ItemStack[] result = new ItemStack[4];
        for (int index = 1; index < 5; index++) {
            result[index - 1] = getItem(index);
        }
        return result;
    }

    default void setItems(ItemStack[] items) {
        if (items.length != 5) {
            throw new IllegalArgumentException("array length must be 5: " + items.length);
        }
        for (int index = 0; index < 5; index++) {
            setItem(index, items[index]);
        }
    }

    default void setArmor(ItemStack[] items) {
        if (items.length != 4) {
            throw new IllegalArgumentException("array length must be 4: " + items.length);
        }
        for (int index = 1; index < 5; index++) {
            setItem(index, items[index - 1]);
        }
    }

    default ItemStack getHelmet() {
        return getItem(4);
    }

    default ItemStack getChestplate() {
        return getItem(3);
    }

    default ItemStack getLeggings() {
        return getItem(2);
    }

    default ItemStack getBoots() {
        return getItem(1);
    }

    default ItemStack getItemInHand() {
        return getItem(0);
    }

    default void setHelmet(ItemStack item) {
        setItem(4, item);
    }

    default void setChestplate(ItemStack item) {
        setItem(3, item);
    }

    default void setLeggings(ItemStack item) {
        setItem(2, item);
    }

    default void setBoots(ItemStack item) {
        setItem(1, item);
    }

    default void setItemInHand(ItemStack item) {
        setItem(0, item);
    }

}
