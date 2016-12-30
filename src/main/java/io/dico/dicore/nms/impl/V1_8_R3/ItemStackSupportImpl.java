package io.dico.dicore.nms.impl.V1_8_R3;

import io.dico.dicore.nms.NItemStackSupport;
import io.dico.dicore.nms.impl.V1_8_R3.nbt.Converter;
import io.dico.dicore.nms.impl.V1_8_R3.nbt.NBTMapImpl;
import io.dico.dicore.nms.nbt.NBTMap;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemStackSupportImpl implements NItemStackSupport {
    @Override
    public ItemStack getNBT(ItemStack stack, Consumer<NBTMap> consumer) {
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(stack);
        NBTMap map = new NBTMapImpl(nms.getTag());
        consumer.accept(map);
        return CraftItemStack.asCraftMirror(nms);
    }

    @Override
    public ItemStack setNBT(ItemStack stack, NBTMap nbt) {
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(stack);
        nms.setTag((NBTTagCompound) Converter.toNMS(nbt));
        return CraftItemStack.asCraftMirror(nms);
    }

}
