package io.dico.dicore.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public interface Gui {
    void open();

    void onInventoryClick(InventoryClickEvent event, int slot);

    InventoryView getView();

    Inventory getInventory();

    Player getPlayer();

    void refreshItems();

    void closed();
}
