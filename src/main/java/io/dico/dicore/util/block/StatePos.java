package io.dico.dicore.util.block;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.BlockProjectileSource;

import java.util.List;
import java.util.Objects;

public class StatePos implements BlockState {
    protected BlockState state;

    public StatePos(BlockState state) {
        this.state = Objects.requireNonNull(state);
    }

    public <T extends BlockState> T getState() {
        return (T) state;
    }

    // INVENTORY HOLDER //

    public boolean isInventoryHolder() {
        return state instanceof InventoryHolder;
    }

    public Inventory getInventory() {
        return ((InventoryHolder) state).getInventory();
    }

    // DISPENSER //

    public boolean isDispenser() {
        return state instanceof Dispenser;
    }

    public boolean dispense() {
        return ((Dispenser) state).dispense();
    }

    public BlockProjectileSource getBlockProjectileSource() {
        return ((Dispenser) state).getBlockProjectileSource();
    }

    // HOPPER //

    public boolean isHopper() {
        return state instanceof Hopper;
    }

    // DROPPER //

    public boolean isDropper() {
        return state instanceof Dropper;
    }

    public void drop() {
        ((Dropper) state).drop();
    }

    // CHEST //

    public boolean isChest() {
        return state instanceof Chest;
    }

    public Inventory getBlockInventory() {
        return ((Chest) state).getBlockInventory();
    }

    // BEACON //

    public boolean isBeacon() {
        return state instanceof Beacon;
    }

    // BANNER //

    public boolean isBanner() {
        return state instanceof Banner;
    }

    public DyeColor getBaseColor() {
        return ((Banner) state).getBaseColor();
    }

    public void setBaseColor(DyeColor dyeColor) {
        ((Banner) state).setBaseColor(dyeColor);
    }

    public List<Pattern> getPatterns() {
        return ((Banner) state).getPatterns();
    }

    public void setPatterns(List<Pattern> list) {
        ((Banner) state).setPatterns(list);
    }

    public void addPattern(Pattern pattern) {
        ((Banner) state).addPattern(pattern);
    }

    public Pattern getPattern(int i) {
        return ((Banner) state).getPattern(i);
    }

    public Pattern removePattern(int i) {
        return ((Banner) state).removePattern(i);
    }

    public void setPattern(int i, Pattern pattern) {
        ((Banner) state).setPattern(i, pattern);
    }

    public int numberOfPatterns() {
        return ((Banner) state).numberOfPatterns();
    }

    // BREWING STAND //

    public boolean isBrewingStand() {
        return state instanceof BrewingStand;
    }

    public int getBrewingTime() {
        return ((BrewingStand) state).getBrewingTime();
    }

    public void setBrewingTime(int i) {
        ((BrewingStand) state).setBrewingTime(i);
    }

    // COMMAND BLOCK //

    public boolean isCommandBlock() {
        return state instanceof CommandBlock;
    }

    public String getCommand() {
        return ((CommandBlock) state).getCommand();
    }

    public void setCommand(String s) {
        ((CommandBlock) state).setCommand(s);
    }

    public String getName() {
        return ((CommandBlock) state).getName();
    }

    public void setName(String s) {
        ((CommandBlock) state).setName(s);
    }

    // CREATURE SPAWNER //

    public boolean isCreatureSpawner() {
        return state instanceof CreatureSpawner;
    }

    public CreatureType getCreatureType() {
        return ((CreatureSpawner) state).getCreatureType();
    }

    public EntityType getSpawnedType() {
        return ((CreatureSpawner) state).getSpawnedType();
    }

    public void setSpawnedType(EntityType entityType) {
        ((CreatureSpawner) state).setSpawnedType(entityType);
    }

    public void setCreatureType(CreatureType creatureType) {
        ((CreatureSpawner) state).setCreatureType(creatureType);
    }

    public String getCreatureTypeId() {
        return ((CreatureSpawner) state).getCreatureTypeId();
    }

    public void setCreatureTypeByName(String s) {
        ((CreatureSpawner) state).setCreatureTypeByName(s);
    }

    public String getCreatureTypeName() {
        return ((CreatureSpawner) state).getCreatureTypeName();
    }

    public void setCreatureTypeId(String s) {
        ((CreatureSpawner) state).setCreatureTypeId(s);
    }

    public int getDelay() {
        return ((CreatureSpawner) state).getDelay();
    }

    public void setDelay(int i) {
        ((CreatureSpawner) state).setDelay(i);
    }

    // FURNACE //

    public boolean isFurnace() {
        return state instanceof Furnace;
    }

    public short getBurnTime() {
        return ((Furnace) state).getBurnTime();
    }

    public void setBurnTime(short i) {
        ((Furnace) state).setBurnTime(i);
    }

    public short getCookTime() {
        return ((Furnace) state).getCookTime();
    }

    public void setCookTime(short i) {
        ((Furnace) state).setCookTime(i);
    }

    // JUKEBOX //

    public boolean isJukebox() {
        return state instanceof Jukebox;
    }

    public boolean isPlaying() {
        return ((Jukebox) state).isPlaying();
    }

    public boolean eject() {
        return ((Jukebox) state).eject();
    }

    public Material getPlaying() {
        return ((Jukebox) state).getPlaying();
    }

    public void setPlaying(Material material) {
        ((Jukebox) state).setPlaying(material);
    }

    // NOTE BLOCK//

    public boolean isNoteBlock() {
        return state instanceof NoteBlock;
    }

    public Note getNote() {
        return ((NoteBlock) state).getNote();
    }

    public byte getRawNote() {
        return ((NoteBlock) state).getRawNote();
    }

    public void setNote(Note note) {
        ((NoteBlock) state).setNote(note);
    }

    public void setRawNote(byte b) {
        ((NoteBlock) state).setRawNote(b);
    }

    public boolean play() {
        return ((NoteBlock) state).play();
    }

    public boolean play(byte b, byte b1) {
        return ((NoteBlock) state).play(b, b1);
    }

    public boolean play(Instrument instrument, Note note) {
        return ((NoteBlock) state).play(instrument, note);
    }

    // SIGN //

    public boolean isSign() {
        return state instanceof Sign;
    }

    public String[] getLines() {
        return ((Sign) state).getLines();
    }

    public String getLine(int i) throws IndexOutOfBoundsException {
        return ((Sign) state).getLine(i);
    }

    public void setLine(int i, String s) throws IndexOutOfBoundsException {
        ((Sign) state).setLine(i, s);
    }

    // SKULL //

    public boolean isSkull() {
        return state instanceof Skull;
    }

    public boolean hasOwner() {
        return ((Skull) state).hasOwner();
    }

    public String getOwner() {
        return ((Skull) state).getOwner();
    }

    public boolean setOwner(String s) {
        return ((Skull) state).setOwner(s);
    }

    public BlockFace getRotation() {
        return ((Skull) state).getRotation();
    }

    public void setRotation(BlockFace blockFace) {
        ((Skull) state).setRotation(blockFace);
    }

    public SkullType getSkullType() {
        return ((Skull) state).getSkullType();
    }

    public void setSkullType(SkullType skullType) {
        ((Skull) state).setSkullType(skullType);
    }

    // BLOCK STATE //

    @Override
    public Block getBlock() {
        return state.getBlock();
    }

    @Override
    public World getWorld() {
        return state.getWorld();
    }

    @Override
    public int getX() {
        return state.getX();
    }

    @Override
    public int getY() {
        return state.getY();
    }

    @Override
    public int getZ() {
        return state.getZ();
    }

    @Override
    public Location getLocation() {
        return state.getLocation();
    }

    @Override
    public Location getLocation(Location location) {
        return state.getLocation(location);
    }

    @Override
    public Chunk getChunk() {
        return state.getChunk();
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) {
        state.setMetadata(s, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String s) {
        return state.getMetadata(s);
    }

    @Override
    public boolean hasMetadata(String s) {
        return state.hasMetadata(s);
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {
        state.removeMetadata(s, plugin);
    }

    @Override
    public MaterialData getData() {
        return state.getData();
    }

    @Override
    public Material getType() {
        return state.getType();
    }

    @Override
    public int getTypeId() {
        return state.getTypeId();
    }

    @Override
    public byte getLightLevel() {
        return state.getLightLevel();
    }

    @Override
    public void setData(MaterialData materialData) {
        state.setData(materialData);
    }

    @Override
    public void setType(Material material) {
        state.setType(material);
    }

    @Override
    public boolean setTypeId(int id) {
        return state.setTypeId(id);
    }

    @Override
    public boolean update() {
        return state.update();
    }

    @Override
    public boolean update(boolean b) {
        return state.update(b);
    }

    @Override
    public boolean update(boolean b, boolean b1) {
        return state.update(b, b1);
    }

    @Override
    public byte getRawData() {
        return state.getRawData();
    }

    @Override
    public void setRawData(byte b) {
        state.setRawData(b);
    }

    @Override
    public boolean isPlaced() {
        return state.isPlaced();
    }

}
