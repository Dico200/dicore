package io.dico.dicore.gui;

import io.dico.dicore.util.Registrator;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;

public class GuiDriver {
    private Map<InventoryView, Gui> openGuis = new HashMap<>();
    
    public GuiDriver(Registrator registrator) {
        registrator.registerListener(InventoryCloseEvent.class, EventPriority.HIGHEST, true, this::onInventoryClose);
        registrator.registerListener(InventoryClickEvent.class, EventPriority.HIGHEST, true, this::onInventoryClick);
    }
    
    public void guiOpened(Gui gui) {
        openGuis.put(gui.getView(), gui);
    }

    private void onInventoryClose(InventoryCloseEvent event) {
        Gui gui = openGuis.get(event.getView());
        if (gui != null) {
            gui.closed();
        }
    }
    
    public void guiClosed(Gui gui) {
        openGuis.remove(gui.getView(), gui);
    }

    private void onInventoryClick(InventoryClickEvent event) {
        Gui gui = openGuis.get(event.getView());
        if (gui != null) {
            gui.onInventoryClick(event, event.getRawSlot());
        }
    }
    
}
