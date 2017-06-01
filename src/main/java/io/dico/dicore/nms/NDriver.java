package io.dico.dicore.nms;

import io.dico.dicore.nms.impl.unknown.Driver_UNKNOWN;
import io.dico.dicore.nms.impl.v1_7_R4.Driver_v1_7_R4;
import io.dico.dicore.nms.impl.v1_8_R3.Driver_v1_8_R3;
import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    
    PotionEffect getActiveEffect(LivingEntity entity, PotionEffectType type);

    NWorld getWorld(World world);
    
    boolean isInWater(Entity entity);
    
    int getFoodRestored(Material item);
    
    float getSaturationModifier(Material item);
    
    long getLastActionTime(Player player);
    
    Location generateRandomPosition(Creature creature, int horizontalDistance, int verticalDistance);

    void sendSoundPacket(Player player, Sound sound, float volume, float pitch);

    void sendSoundPacket(Player player, Sound sound, Location location, float volume, float pitch);
    
    boolean commenceMobAttack(Monster monster, Entity target);
    
    void commencePlayerAttack(Player player, Entity target);

    enum Version {
        v1_7_R4,
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
                case v1_7_R4:
                    driver = new Driver_v1_7_R4();
                    break;
                case v1_8_R3:
                    driver = new Driver_v1_8_R3();
                    break;
                default:
                    driver = new Driver_UNKNOWN();
            }
        }

    }

}
