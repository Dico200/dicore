package io.dico.dicore.nms.impl.v1_8_R3;

import io.dico.dicore.nms.NCreatureEquipment;
import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.NServer;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.nms.impl.v1_8_R3.nbt.NBTMap_v1_8_R3;
import io.dico.dicore.nms.nbt.NBTMap;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
        NBTTagCompound tag = nms.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
            nms.setTag(tag);
        }
        NBTMap map = new NBTMap_v1_8_R3(tag);
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
    
    @Override
    public boolean isInWater(Entity entity) {
        return ((CraftEntity) entity).getHandle().inWater;
    }
    
    @Override
    public int getFoodRestored(Material item) {
        Item nmsItem = Item.getById(item.getId());
        if (nmsItem instanceof ItemFood) {
            return ((ItemFood) nmsItem).getNutrition(null);
        }
        return 0;
    }
    
    @Override
    public float getSaturationModifier(Material item) {
        Item nmsItem = Item.getById(item.getId());
        if (nmsItem instanceof ItemFood) {
            return ((ItemFood) nmsItem).getSaturationModifier(null);
        }
        return 0;
    }
    
    @Override
    public long getLastActionTime(Player player) {
        return ((CraftPlayer) player).getHandle().D();
    }
    
    @Override
    public Location generateRandomPosition(Creature creature, int horizontalDistance, int verticalDistance) {
        Vec3D res = RandomPositionGenerator.a(((CraftCreature) creature).getHandle(), horizontalDistance, verticalDistance);
        Location loc = creature.getLocation();
        loc.setX(res.a);
        loc.setY(res.b);
        loc.setZ(res.c);
        return loc;
    }
    
}
