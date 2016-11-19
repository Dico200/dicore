package io.dico.dicore.event;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class EventFunctions {

    private static Function<Event, World> WORLD_FROM_ENTITY = event -> ((EntityEvent) event).getEntity().getWorld();
    private static Function<Event, World> WORLD_FROM_PLAYER = event -> ((PlayerEvent) event).getPlayer().getWorld();
    private static Function<Event, World> WORLD_FROM_WORLD = event -> ((WorldEvent) event).getWorld();
    private static Function<Event, World> WORLD_FROM_BLOCK = event -> ((BlockEvent) event).getBlock().getWorld();
    private static final InspectorTable inspectors;

    public static <T extends Event> Function<T, World> worldFunctionFor(Class<T> clazz) {

        if (PlayerEvent.class.isAssignableFrom(clazz)) {
            return event -> ((PlayerEvent) event).getPlayer().getWorld();
        } else if (EntityEvent.class.isAssignableFrom(clazz)) {
            return event -> ((EntityEvent) event).getEntity().getWorld();
        }
        return null;
    }

    static {
        inspectors = new InspectorTable() {
            {
                addInspector(World.class, WorldEvent.class, WorldEvent::getWorld);
                addInspector(World.class, WeatherEvent.class, WeatherEvent::getWorld);
                addInspector(World.class, BlockEvent.class, event -> event.getBlock().getWorld());
                addInspector(World.class, PlayerEvent.class, event -> event.getPlayer().getWorld());
                addInspector(World.class, PlayerLeashEntityEvent.class, event -> event.getEntity().getWorld());
                addInspector(World.class, EntityEvent.class, event -> event.getEntity().getWorld());
                addInspector(World.class, HangingEvent.class, event -> event.getEntity().getWorld());
                addInspector(World.class, VehicleEvent.class, event -> event.getVehicle().getWorld());
                addInspector(World.class, InventoryEvent.class, event -> getWorldOf(event.getInventory().getHolder()));
                addInspector(World.class, InventoryMoveItemEvent.class, event -> getWorldOf(event.getInitiator().getHolder()));
                addInspector(World.class, InventoryPickupItemEvent.class, event -> event.getItem().getWorld());

                addInspector(Inventory.class, InventoryEvent.class, InventoryEvent::getInventory);
                addInspector(Inventory.class, InventoryMoveItemEvent.class, InventoryMoveItemEvent::getInitiator);
                addInspector(Inventory.class, InventoryPickupItemEvent.class, InventoryPickupItemEvent::getInventory);

                addInspector(Item.class, InventoryPickupItemEvent.class, InventoryPickupItemEvent::getItem);
                addInspector(Item.class, ItemSpawnEvent.class, ItemSpawnEvent::getEntity);
                addInspector(Item.class, ItemDespawnEvent.class, ItemDespawnEvent::getEntity);
                addInspector(Item.class, ItemMergeEvent.class, ItemMergeEvent::getEntity);

                addInspector(Block.class, BlockEvent.class, BlockEvent::getBlock);

                addInspector(Player.class, PlayerEvent.class, PlayerEvent::getPlayer);

                addInspector(UUID.class, PlayerEvent.class, event -> event.getPlayer().getUniqueId());
                addInspector(UUID.class, AsyncPlayerPreLoginEvent.class, AsyncPlayerPreLoginEvent::getUniqueId);
                addInspector(UUID.class, EntityEvent.class, event -> event.getEntity().getUniqueId());
                addInspector(UUID.class, HangingEvent.class, event -> event.getEntity().getUniqueId());
                addInspector(UUID.class, VehicleEvent.class, event -> event.getVehicle().getUniqueId());
            }
        };
    }

    private static World getWorldOf(InventoryHolder holder) {
        if (holder instanceof BlockState)
            return ((BlockState) holder).getWorld();
        if (holder instanceof Entity)
            return ((Entity) holder).getWorld();
        if (holder instanceof DoubleChest)
            return ((DoubleChest) holder).getWorld();
        return null;
    }

}

