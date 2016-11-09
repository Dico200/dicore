package io.dico.dicore.util;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;

public class SpigotUtil {

    public static World matchWorld(String input) {
        try {
            UUID uid = UUID.fromString(input);
            World world = Bukkit.getWorld(uid);
            if (world != null) {
                return world;
            }
        } catch (IllegalArgumentException ignored) {}

        World result = Bukkit.getWorld(input);
        if (result == null) {
            input = input.toLowerCase().replace("_", "").replaceAll("-|_", "");
            for (World world : Bukkit.getWorlds()) {
                if (world.getName().toLowerCase().equals(input)) {
                    result = world;
                    break;
                }
            }
        }

        return result;
    }

}
