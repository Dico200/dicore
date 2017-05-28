package io.dico.dicore.util;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.JsonLoadable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Save and load {@link ItemStack} in json format
 * Any additional NBT data not included by {@link ItemMeta} is discarded.
 */
public class ItemProperties implements JsonLoadable {
    private int id;
    private byte data;
    private int amount;
    private Map<Enchantment, Integer> enchantments;
    private List<String> lore;
    private String displayName;
    private boolean unbreakable;
    
    public ItemProperties() {
    }
    
    public ItemProperties(ItemStack item) {
        id = item.getTypeId();
        data = item.getData().getData();
        amount = item.getAmount();
        enchantments = new HashMap<>();
        
        ItemMeta meta = StorageForwardingMeta.ensureNotStored(item.getItemMeta());
        if (meta.hasEnchants()) {
            enchantments.putAll(meta.getEnchants());
        }
        
        if (meta.hasLore()) {
            lore = meta.getLore();
        }
        
        if (meta.hasDisplayName()) {
            displayName = meta.getDisplayName();
        }
        
        unbreakable = meta.spigot().isUnbreakable();
    }
    
    public ItemStack toItemStack() {
        ItemStack result = new ItemStack(id, amount, data);
        
        ItemMeta meta = StorageForwardingMeta.ensureNotStored(result.getItemMeta());
        if (enchantments != null) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
        
        if (lore != null) {
            meta.setLore(lore);
        }
        
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        
        meta.spigot().setUnbreakable(unbreakable);
        
        result.setItemMeta(StorageForwardingMeta.toOriginal(meta));
        return result;
    }
    
    @Override
    public void writeTo(JsonWriter writer) throws IOException {
        writer.beginObject();
        
        writer.name("id").value(id);
        writer.name("data").value(data);
        writer.name("amount").value(amount);
        
        if (displayName != null) {
            writer.name("displayName").value(displayName);
        }
        
        if (enchantments != null) {
            writer.name("enchantments");
            Map<Enchantment, Integer> enchantments = this.enchantments;
            Map<String, Integer> stringKeys = new HashMap<>(enchantments.size());
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                stringKeys.put(entry.getKey().getName(), entry.getValue().intValue());
            }
            
            JsonUtil.insert(writer, stringKeys);
        }
        
        if (lore != null) {
            writer.name("lore");
            JsonUtil.insert(writer, lore);
        }
        
        if (unbreakable) {
            writer.name("unbreakable").value(true);
        }
        
        writer.endObject();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void loadFrom(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            final String key = reader.nextName();
            switch (key) {
                case "id":
                    id = reader.nextInt();
                    break;
                case "data":
                    data = (byte) reader.nextInt();
                    break;
                case "amount":
                    amount = reader.nextInt();
                    break;
                case "unbreakable":
                    unbreakable = reader.nextBoolean();
                    break;
                case "enchantments": {
                    if (enchantments == null) {
                        enchantments = new HashMap<>();
                    } else if (!enchantments.isEmpty()) {
                        enchantments.clear();
                    }
                    Map<String, Double> read = (Map<String, Double>) JsonUtil.read(reader);
                    if (read != null) {
                        for (Map.Entry<String, Double> entry : read.entrySet()) {
                            Enchantment ench = Enchantment.getByName(entry.getKey());
                            if (ench != null) {
                                int level = entry.getValue().intValue();
                                enchantments.put(ench, level);
                            }
                        }
                    }
                    break;
                }
                case "lore":
                    lore = (List<String>) JsonUtil.read(reader);
                    break;
                case "displayName":
                    displayName = reader.nextString();
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
    }
    
    public int getId() {
        return id;
    }
    
    public byte getData() {
        return data;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }
    
    public List<String> getLore() {
        return lore;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isUnbreakable() {
        return unbreakable;
    }
    
    public ItemProperties setId(int id) {
        this.id = id;
        return this;
    }
    
    public ItemProperties setData(byte data) {
        this.data = data;
        return this;
    }
    
    public ItemProperties setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemProperties setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }
    
    public ItemProperties setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }
    
    public ItemProperties setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
    
    public ItemProperties setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }
    
}
