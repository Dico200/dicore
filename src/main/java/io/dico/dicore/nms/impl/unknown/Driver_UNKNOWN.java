package io.dico.dicore.nms.impl.unknown;

import io.dico.dicore.nms.NCreatureEquipment;
import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.NServer;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.nms.impl.unknown.nbt.NBTMap_UNKNOWN;
import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class Driver_UNKNOWN implements NDriver {
    final NServer server = new Server_UNKNOWN();
    final NWorld world = new World_UNKNOWN();

    @Override
    public NServer getServer() {
        return server;
    }

    @Override
    public ItemStack exploreNBT(ItemStack stack, Predicate<NBTMap> changed) {
        changed.test(new NBTMap_UNKNOWN());
        return stack;
    }

    @Override
    public NCreatureEquipment getCreatureEquipment(Creature creature) {
        return new CreatureEquipment_UNKNOWN(creature);
    }

    @Override
    public NWorld getWorld(World world) {
        return world == null ? null : this.world;
    }

}
