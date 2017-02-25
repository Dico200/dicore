package io.dico.dicore.nms.impl.v1_8_R3;

import io.dico.dicore.nms.NCreatureEquipment;
import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.NServer;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.nms.impl.v1_8_R3.nbt.NBTMap_v1_8_R3;
import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Driver_v1_8_R3 implements NDriver {
    private final NServer server = new Server_v1_8_R3();
    private final Map<World, NWorld> worlds = new HashMap<>();

    @Override
    public NServer getServer() {
        return server;
    }

    @Override
    public ItemStack exploreNBT(ItemStack item, Predicate<NBTMap> consumer) {
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTMap map = new NBTMap_v1_8_R3(nms.getTag());
        if (consumer.test(map)) {
            return CraftItemStack.asCraftMirror(nms);
        }
        return item;
    }
    
    @Override
    public NBTMap getNBT(ItemStack item) {
        return new NBTMap_v1_8_R3(CraftItemStack.asNMSCopy(item).getTag());
    }
    
    @Override
    public ItemStack setNBT(ItemStack item, NBTMap map) {
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
        nms.setTag(((NBTMap_v1_8_R3) map).base);
        return CraftItemStack.asCraftMirror(nms);
    }
    
    /*
    @Override
    public void setTitleOfOpenInventory(Player player, String title) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(handle.activeContainer.windowId,
                "minecraft:" + handle.activeContainer.getBukkitView().getType().name().toLowerCase(),
                new ChatComponentText(title), handle.activeContainer.a().size());
        handle.playerConnection.sendPacket(packet);
    }
    */
    
    @Override
    public NCreatureEquipment getCreatureEquipment(Creature creature) {
        return new CreatureEquipment_v1_8_R3(creature);
    }

    @Override
    public NWorld getWorld(World world) {
        return worlds.computeIfAbsent(world, World_v1_8_R3::new);
    }

}
