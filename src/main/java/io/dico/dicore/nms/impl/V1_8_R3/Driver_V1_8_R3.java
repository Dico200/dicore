package io.dico.dicore.nms.impl.V1_8_R3;

import io.dico.dicore.nms.NCreatureEquipment;
import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.NServer;
import io.dico.dicore.nms.impl.V1_8_R3.nbt.NBTMap_V1_8_R3;
import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class Driver_V1_8_R3 implements NDriver {
    private final NServer server = new Server_V1_8_R3();

    @Override
    public NServer getServer() {
        return server;
    }

    @Override
    public ItemStack exploreNBT(ItemStack stack, Predicate<NBTMap> consumer) {
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(stack);
        NBTMap map = new NBTMap_V1_8_R3(nms.getTag());
        if (consumer.test(map)) {
            return CraftItemStack.asCraftMirror(nms);
        }
        return stack;
    }

    @Override
    public NCreatureEquipment getCreatureEquipment(Creature creature) {
        return new CreatureEquipment_V1_8_R3(creature);
    }

}