package io.dico.dicore.nms;

import io.dico.dicore.nms.nbt.NBTMap;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface NItemStackSupport {

    ItemStack getNBT(ItemStack stack, Consumer<NBTMap> consumer);

    ItemStack setNBT(ItemStack stack, NBTMap nbt);

}
