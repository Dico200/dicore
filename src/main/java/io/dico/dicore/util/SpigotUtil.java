package io.dico.dicore.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;

import java.util.Map;
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

    public static Block getSupportingBlock(Block block) {
        MaterialData data = block.getState().getData();
        if (data instanceof Attachable) {
            BlockFace attachedOn = ((Attachable) data).getAttachedFace();
            return block.getRelative(attachedOn);
        }
        return null;
    }

    public static boolean isItemPresent(ItemStack stack) {
        return stack != null && stack.getType() != Material.AIR && stack.getAmount() > 0;
    }

    public static boolean removeItems(Inventory from, ItemStack item, int amount) {
        for (Map.Entry<Integer, ? extends ItemStack> entry : from.all(item.getType()).entrySet()) {
            ItemStack stack = entry.getValue();
            if (item.isSimilar(stack)) {
                amount -= stack.getAmount();
                int stackAmount = -Math.min(0, amount);
                if (stackAmount == 0) {
                    from.setItem(entry.getKey(), null);
                } else {
                    stack.setAmount(stackAmount);
                }
            }
        }
        return amount <= 0;
    }

}
