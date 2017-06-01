package io.dico.dicore.nms.impl.v1_7_R4;

import io.dico.dicore.nms.NCreatureEquipment;
import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.NServer;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.nms.nbt.NBTList;
import io.dico.dicore.nms.nbt.NBTMap;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.server.v1_7_R4.Item;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftSound;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Driver_v1_7_R4 implements NDriver {
    private final NServer server = new ServerImpl();
    private final Map<org.bukkit.World, NWorld> worlds = new HashMap<>();

    @Override
    public NServer getServer() {
        return server;
    }
    
    @Override
    public NBTList newNbtList() {
        return new NBTListImpl();
    }
    
    @Override
    public NBTMap newNbtMap() {
        return new NBTMapImpl();
    }
    
    @Override
    public ItemStack exploreNBT(ItemStack item, Predicate<NBTMap> consumer) {
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
            nms.setTag(tag);
        }
        NBTMap map = new NBTMapImpl(tag);
        if (consumer.test(map)) {
            return CraftItemStack.asCraftMirror(nms);
        }
        return item;
    }
    
    @Override
    public NBTMap getNBT(ItemStack item) {
        return new NBTMapImpl(CraftItemStack.asNMSCopy(item).getTag());
    }
    
    @Override
    public ItemStack setNBT(ItemStack item, NBTMap map) {
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(item);
        nms.setTag(((NBTMapImpl) map).base);
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
        return new CreatureEquipmentImpl(creature);
    }
    
    @Override
    public PotionEffect getActiveEffect(LivingEntity entity, PotionEffectType type) {
        MobEffect active = (MobEffect) ((CraftLivingEntity) entity).getHandle().effects.get(type.getId());
        if (active == null) {
            return null;
        }
        return new PotionEffect(type, active.getDuration(), active.getAmplifier(), active.isAmbient());
    }
    
    @Override
    public NWorld getWorld(org.bukkit.World world) {
        return worlds.computeIfAbsent(world, WorldImpl::new);
    }
    
    @Override
    public boolean commenceMobAttack(Monster bukkitMonster, Entity bukkitTarget) {
        EntityMonster monster = ((CraftMonster) bukkitMonster).getHandle();
        return monster.n(((CraftEntity) bukkitTarget).getHandle());
    }
    
    @Override
    public void commencePlayerAttack(Player bukkitPlayer, Entity bukkitTarget) {
        EntityPlayer player = ((CraftPlayer) bukkitPlayer).getHandle();
        player.attack(((CraftEntity) bukkitTarget).getHandle());
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

    @Override
    public void sendSoundPacket(Player player, Sound sound, float volume, float pitch) {
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        p.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(CraftSound.getSound(sound), p.locX, p.locY, p.locZ, volume, pitch));
    }

    @Override
    public void sendSoundPacket(Player player, Sound sound, Location loc, float volume, float pitch) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(CraftSound.getSound(sound), loc.getX(), loc.getY(), loc.getZ(), volume, pitch));
    }
    
}
