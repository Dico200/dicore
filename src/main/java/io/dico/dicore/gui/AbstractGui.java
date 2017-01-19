package io.dico.dicore.gui;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

public abstract class AbstractGui implements Gui {
    protected final GuiDriver driver;
    private Player player;
    private boolean blockChanges;
    private Inventory inventory;
    private InventoryView view;
    private boolean referred;
    private TIntObjectMap<Collection<ActionedSlot>> listeners;
    
    public AbstractGui(GuiDriver driver, Player player, boolean blockChanges) {
        this.driver = Objects.requireNonNull(driver);
        this.player = player;
        this.blockChanges = blockChanges;
        listeners = new TIntObjectHashMap<>(5, .5F, 2195);
        inventory = createInventory();
    }
    
    protected abstract Inventory createInventory();
    
    protected abstract void refreshItems(Inventory inventory);
    
    @Override
    public void open() {
        refreshItems();
        view = player.openInventory(inventory);
        driver.guiOpened(this);
    }
    
    @Override
    public final void refreshItems() {
        refreshItems(inventory);
    }
    
    @Override
    public void closed() {
        if (!referred) {
            driver.guiClosed(this);
            cleanup();
            inventory = null;
            view = null;
        } else {
            referred = false;
        }
    }
    
    @Override
    public final Player getPlayer() {
        return player;
    }
    
    @Override
    public final Inventory getInventory() {
        return inventory;
    }
    
    @Override
    public final InventoryView getView() {
        return view;
    }
    
    @Override
    public final void onInventoryClick(InventoryClickEvent event, int slot) {
        if (view == event.getView()) {
            if (blockChanges) {
                event.setResult(Event.Result.DENY);
            }
            
            for (ActionedSlot listener : getListeners(listeners.getNoEntryKey(), false)) {
                listener.onClick(event, slot);
            }
            
            for (ActionedSlot listener : getListeners(slot, false)) {
                listener.onClick(event, slot);
            }
        }
    }
    
    protected void cleanup() {
        
    }
    
    protected final void openOtherGui(Gui otherGui) {
        if (otherGui == this) {
            return;
        }
        if (otherGui.getPlayer() == player) {
            referred = true;
        }
        otherGui.open();
    }
    
    private Collection<ActionedSlot> getListeners(int key, boolean ensurePresent) {
        Collection<ActionedSlot> result = listeners.get(key);
        if (result == null) {
            if (ensurePresent) {
                result = new LinkedList<>();
                listeners.put(key, result);
            } else {
                result = Collections.emptyList();
            }
        }
        return result;
    }
    
    protected final void addListener(ClickType type, ActionListener listener, int... slots) {
        ActionedSlot action = new ActionedSlot(listener, type);
        if (slots.length == 0) {
            getListeners(listeners.getNoEntryKey(), true).add(action);
        } else {
            for (int slot : slots) {
                getListeners(slot, true).add(action);
            }
        }
    }
    
    protected interface ActionListener {
        void onClick(InventoryClickEvent event, int slot);
    }
    
    private static final class ActionedSlot {
        
        private ActionListener listener;
        private ClickType clickType;
        
        ActionedSlot(ActionListener listener, ClickType clickType) {
            this.listener = listener;
            this.clickType = clickType;
        }
        
        void onClick(InventoryClickEvent event, int slot) {
            if (clickType == null || clickType == event.getClick()) {
                listener.onClick(event, slot);
            }
        }
    }
    
}
