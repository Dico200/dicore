package io.dico.dicore.nms;

import io.dico.dicore.nms.impl.unknown.Driver_UNKNOWN;
import io.dico.dicore.nms.impl.v1_8_R3.Driver_v1_8_R3;
import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public interface NDriver {

    static Version getVersion() {
        return Version.getInstance();
    }

    static boolean isWorking() {
        return getVersion() != Version.UNKNOWN;
    }

    static NDriver getInstance() {
        return Version.getDriver();
    }

    NServer getServer();

    ItemStack exploreNBT(ItemStack item, Predicate<NBTMap> changed);
    
    NBTMap getNBT(ItemStack item);
    
    ItemStack setNBT(ItemStack item, NBTMap map);
    
    //void setTitleOfOpenInventory(Player player, String title);

    NCreatureEquipment getCreatureEquipment(Creature creature);

    NWorld getWorld(World world);
    
    boolean isInWater(Entity entity);
    
    int getFoodRestored(Material item);
    
    float getSaturationModifier(Material item);
    
    long getLastActionTime(Player player);

    enum Version {
        v1_8_R3,
        UNKNOWN;

        private static final Version instance;
        private static final NDriver driver;

        public static Version getInstance() {
            return instance;
        }

        public static NDriver getDriver() {
            return driver;
        }

        static {
            String serverClass = Bukkit.getServer().getClass().getName();
            String[] split = serverClass.split("\\.");
            Version inst = UNKNOWN;
            if (split.length >= 2) {
                try {
                    inst = valueOf(split[split.length - 2]);
                } catch (Exception ignored) {
                }
            }
            instance = inst;

            switch (instance) {
                case v1_8_R3:
                    driver = new Driver_v1_8_R3();
                    break;
                default:
                    driver = new Driver_UNKNOWN();
            }
        }

    }

}
