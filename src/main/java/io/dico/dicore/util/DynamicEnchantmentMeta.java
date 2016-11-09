package io.dico.dicore.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DynamicEnchantmentMeta implements ItemMeta {

    private final ItemMeta meta;
    private final boolean stored;

    public DynamicEnchantmentMeta(ItemMeta meta) {
        this.meta = meta instanceof DynamicEnchantmentMeta ? ((DynamicEnchantmentMeta) meta).getActualMeta() : meta;
        this.stored = this.meta instanceof EnchantmentStorageMeta;
    }

    public ItemMeta getActualMeta() {
        return meta;
    }

    @Override
    public boolean addEnchant(Enchantment ench, int level, boolean ignoreLevelRestrictions) {
        if (stored)
            return ((EnchantmentStorageMeta) meta).addStoredEnchant(ench, level, ignoreLevelRestrictions);
        return meta.addEnchant(ench, level, ignoreLevelRestrictions);
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        if (stored)
            return ((EnchantmentStorageMeta) meta).getStoredEnchants();
        return meta.getEnchants();
    }

    @Override
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addItemFlags(ItemFlag... arg0) {
        meta.addItemFlags(arg0);
    }

    @Override
    public ItemMeta clone() {
        return new DynamicEnchantmentMeta(meta.clone());
    }

    @Override
    public String getDisplayName() {
        return meta.getDisplayName();
    }

    @Override
    public int getEnchantLevel(Enchantment arg0) {
        if (stored)
            return ((EnchantmentStorageMeta) meta).getStoredEnchantLevel(arg0);
        return meta.getEnchantLevel(arg0);
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        return meta.getItemFlags();
    }

    @Override
    public List<String> getLore() {
        return meta.getLore();
    }

    @Override
    public boolean hasConflictingEnchant(Enchantment arg0) {
        if (stored)
            return ((EnchantmentStorageMeta) meta).hasConflictingStoredEnchant(arg0);
        return meta.hasConflictingEnchant(arg0);
    }

    @Override
    public boolean hasDisplayName() {
        return meta.hasDisplayName();
    }

    @Override
    public boolean hasEnchant(Enchantment arg0) {
        if (stored)
            return ((EnchantmentStorageMeta) meta).hasEnchant(arg0);
        return meta.hasEnchant(arg0);
    }

    @Override
    public boolean hasEnchants() {
        if (stored)
            return ((EnchantmentStorageMeta) meta).hasStoredEnchants();
        return meta.hasEnchants();
    }

    @Override
    public boolean hasItemFlag(ItemFlag arg0) {
        return meta.hasItemFlag(arg0);
    }

    @Override
    public boolean hasLore() {
        return meta.hasLore();
    }

    @Override
    public boolean removeEnchant(Enchantment arg0) {
        if (stored)
            return ((EnchantmentStorageMeta) meta).removeStoredEnchant(arg0);
        return meta.removeEnchant(arg0);
    }

    @Override
    public void removeItemFlags(ItemFlag... arg0) {
        meta.removeItemFlags(arg0);
    }

    @Override
    public void setDisplayName(String arg0) {
        meta.setDisplayName(arg0);
    }

    @Override
    public void setLore(List<String> arg0) {
        meta.setLore(arg0);
    }

    @Override
    public Spigot spigot() {
        return meta.spigot();
    }

}