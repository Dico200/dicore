package io.dico.dicore.nms;

import io.dico.dicore.nms.impl.V1_8_R3.Driver_V1_8_R3;
import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public interface NDriver {

    static NDriver getInstance() {
        return Version.getDriver();
    }

    NServer getServer();

    ItemStack exploreNBT(ItemStack stack, Predicate<NBTMap> changed);

    NCreatureEquipment getCreatureEquipment(Creature creature);

    NWorld getWorld(World world);

    enum Version {

        V1_8_R3;

        private static final Version instance;
        private static final NDriver driver;

        public static Version getInstance() {
            if (instance == null) {
                throw new IllegalStateException();
            }
            return instance;
        }

        public static NDriver getDriver() {
            if (driver == null) {
                throw new IllegalStateException();
            }
            return driver;
        }

        static {
            String serverClass = Bukkit.getServer().getClass().getName();
            String[] split = serverClass.split("\\.");
            if (split.length >= 2) {
                instance = valueOf(split[split.length - 2]);

                switch (instance) {
                    case V1_8_R3:
                        driver = new Driver_V1_8_R3();
                        break;
                    default:
                        driver = null;
                }

            } else {
                throw new IllegalStateException();
            }
        }

    }

}
