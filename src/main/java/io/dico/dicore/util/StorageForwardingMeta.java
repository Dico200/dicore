package io.dico.dicore.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class StorageForwardingMeta implements ItemMeta {
    private final EnchantmentStorageMeta delegate;
    
    public StorageForwardingMeta(EnchantmentStorageMeta delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }
    
    public static ItemMeta ensureNotStored(ItemMeta meta) {
        return meta instanceof EnchantmentStorageMeta ? new StorageForwardingMeta((EnchantmentStorageMeta) meta) : meta;
    }
    
    public static ItemMeta toOriginal(ItemMeta meta) {
        return meta instanceof StorageForwardingMeta ? ((StorageForwardingMeta) meta).getDelegate() : meta;
    }
    
    public EnchantmentStorageMeta getDelegate() {
        return delegate;
    }
    
    @Override
    public boolean hasDisplayName() {
        return delegate.hasDisplayName();
    }
    
    @Override
    public String getDisplayName() {
        return delegate.getDisplayName();
    }
    
    @Override
    public void setDisplayName(String s) {
        delegate.setDisplayName(s);
    }
    
    @Override
    public boolean hasLore() {
        return delegate.hasLore();
    }
    
    @Override
    public List<String> getLore() {
        return delegate.getLore();
    }
    
    @Override
    public void setLore(List<String> list) {
        delegate.setLore(list);
    }
    
    @Override
    public boolean hasEnchants() {
        return delegate.hasStoredEnchants();
    }
    
    @Override
    public boolean hasEnchant(Enchantment enchantment) {
        return delegate.hasStoredEnchant(enchantment);
    }
    
    @Override
    public int getEnchantLevel(Enchantment enchantment) {
        return delegate.getStoredEnchantLevel(enchantment);
    }
    
    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return delegate.getStoredEnchants();
    }
    
    @Override
    public boolean addEnchant(Enchantment enchantment, int i, boolean b) {
        return delegate.addStoredEnchant(enchantment, i, b);
    }
    
    @Override
    public boolean removeEnchant(Enchantment enchantment) {
        return delegate.removeStoredEnchant(enchantment);
    }
    
    @Override
    public boolean hasConflictingEnchant(Enchantment enchantment) {
        return delegate.hasConflictingStoredEnchant(enchantment);
    }
    
    @Override
    public void addItemFlags(ItemFlag... itemFlags) {
        delegate.addItemFlags(itemFlags);
    }
    
    @Override
    public void removeItemFlags(ItemFlag... itemFlags) {
        delegate.removeItemFlags(itemFlags);
    }
    
    @Override
    public Set<ItemFlag> getItemFlags() {
        return delegate.getItemFlags();
    }
    
    @Override
    public boolean hasItemFlag(ItemFlag itemFlag) {
        return delegate.hasItemFlag(itemFlag);
    }
    
    @Override
    public ItemMeta clone() {
        return new StorageForwardingMeta(delegate.clone());
    }
    
    @Override
    public Spigot spigot() {
        return delegate.spigot();
    }
    
    @Override
    public Map<String, Object> serialize() {
        return delegate.serialize();
    }
    
}
