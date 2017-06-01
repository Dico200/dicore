package io.dico.dicore.nms.impl.v1_8_R3;

import io.dico.dicore.nms.NCreatureEquipment;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

class CreatureEquipmentImpl implements NCreatureEquipment {

    private static final ItemStack NOT_SET = new ItemStack(Material.AIR);

    private static ItemStack fromNMS(net.minecraft.server.v1_8_R3.ItemStack item) {
        return item == null || Item.getId(item.getItem()) == 0 ? null : CraftItemStack.asCraftMirror(item);
    }

    private static net.minecraft.server.v1_8_R3.ItemStack toNMS(ItemStack item) {
        return CraftItemStack.asNMSCopy(item);
    }

    private final EntityInsentient entity;
    private final ItemStack[] cache = new ItemStack[5];

    public CreatureEquipmentImpl(EntityInsentient entity) {
        this.entity = entity;
        refreshCache();
    }

    public CreatureEquipmentImpl(Creature creature) {
        this(((CraftCreature) creature).getHandle());
    }

    @Override
    public Creature getCreature() {
        Entity result = entity.getBukkitEntity();
        if (result instanceof Creature) {
            return (Creature) result;
        }
        return null;
    }

    @Override
    public ItemStack getItem(int index) {
        ItemStack result = cache[index];
        if (result == NOT_SET) {
            result = fromNMS(entity.getEquipment(index));
            cache[index] = result;
        }
        return result;
    }

    @Override
    public void setItem(int index, ItemStack item) {
        entity.setEquipment(index, toNMS(item));
    }

    @Override
    public void refreshCache() {
        Arrays.fill(cache, NOT_SET);
    }

}
