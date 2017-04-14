package io.dico.dicore.nms.impl.unknown;

import io.dico.dicore.nms.NCreatureEquipment;
import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.NServer;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.nms.impl.unknown.nbt.NBTMap_UNKNOWN;
import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
    public ItemStack exploreNBT(ItemStack item, Predicate<NBTMap> changed) {
        changed.test(new NBTMap_UNKNOWN());
        return item;
    }
    
    @Override
    public NBTMap getNBT(ItemStack item) {
        return new NBTMap_UNKNOWN();
    }
    
    @Override
    public ItemStack setNBT(ItemStack item, NBTMap map) {
        return item;
    }
    
    @Override
    public NCreatureEquipment getCreatureEquipment(Creature creature) {
        return new CreatureEquipment_UNKNOWN(creature);
    }

    @Override
    public NWorld getWorld(World world) {
        return world == null ? null : this.world;
    }
    
    @Override
    public boolean isInWater(Entity entity) {
        return false;
    }
    
    @Override
    public int getFoodRestored(Material item) {
        return 0;
    }
    
    @Override
    public float getSaturationModifier(Material item) {
        return 0;
    }
    
    @Override
    public long getLastActionTime(Player player) {
        return System.currentTimeMillis();
    }
    
    @Override
    public Location generateRandomPosition(Creature creature, int horizontalDistance, int verticalDistance) {
        return creature.getLocation();
    }

    @Override
    public void sendSoundPacket(Player player, Sound sound, float volume, float pitch) {

    }

    @Override
    public void sendSoundPacket(Player player, Sound sound, Location location, float volume, float pitch) {

    }

}
