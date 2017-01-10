package io.dico.dicore.util.block;

import com.google.gson.stream.JsonReader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BlockBlockPos extends CorePos<BlockBlockPos> implements Block {

    public BlockBlockPos() {
    }

    public BlockBlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public BlockBlockPos(String worldName, int x, int y, int z) {
        super(worldName, x, y, z);
    }

    public BlockBlockPos(World world, int x, int y, int z) {
        super(world, x, y, z);
    }

    public BlockBlockPos(JsonReader reader) throws IOException {
        super(reader);
    }

    public BlockBlockPos(Map<String, Object> map) {
        super(map);
    }

    public BlockBlockPos(boolean mutable) {
        super(mutable);
    }

    public BlockBlockPos(boolean mutable, int x, int y, int z) {
        super(mutable, x, y, z);
    }

    public BlockBlockPos(boolean mutable, String worldName, int x, int y, int z) {
        super(mutable, worldName, x, y, z);
    }

    public BlockBlockPos(boolean mutable, World world, int x, int y, int z) {
        super(mutable, world, x, y, z);
    }

    public BlockBlockPos(boolean mutable, JsonReader reader) throws IOException {
        super(mutable, reader);
    }

    public BlockBlockPos(boolean mutable, Map<String, Object> map) {
        super(mutable, map);
    }

    public BlockBlockPos(Block block) {
        super(block);
    }

    public BlockBlockPos(BlockState block) {
        super(block);
    }

    public BlockBlockPos(Location loc) {
        super(loc);
    }

    public BlockBlockPos(boolean mutable, Block block) {
        super(mutable, block);
    }

    public BlockBlockPos(boolean mutable, BlockState block) {
        super(mutable, block);
    }

    public BlockBlockPos(boolean mutable, Location loc) {
        super(mutable, loc);
    }

    @Override
    protected BlockBlockPos createNew() {
        return new BlockBlockPos();
    }

    @Override
    protected Metadatable getMetadatable() {
        return getBlock();
    }

    // --------------- BLOCK SPECIFIC ------------------


    @Override
    public final byte getData() {
        return getBlock().getData();
    }

    @Override
    public BlockBlockPos getRelative(int x, int y, int z) {
        return add(x, y, z);
    }

    @Override
    public BlockBlockPos getRelative(BlockFace face) {
        return add(face.getModX(), face.getModY(), face.getModZ());
    }

    @Override
    public BlockBlockPos getRelative(BlockFace face, int multiplier) {
        return add(face.getModX() * multiplier, face.getModX() * multiplier, face.getModZ() * multiplier);
    }

    @Override
    public final Material getType() {
        return getBlock().getType();
    }

    @Override
    public final int getTypeId() {
        return getBlock().getTypeId();
    }

    @Override
    public final byte getLightLevel() {
        return getBlock().getLightLevel();
    }

    @Override
    public final byte getLightFromSky() {
        return getBlock().getLightFromSky();
    }

    @Override
    public final byte getLightFromBlocks() {
        return getBlock().getLightFromBlocks();
    }

    @Override
    public final void setData(byte data) {
        getBlock().setData(data);
    }

    @Override
    public final void setData(byte data, boolean applyPhysics) {
        getBlock().setData(data, applyPhysics);
    }

    @Override
    public final void setType(Material material) {
        getBlock().setType(material);
    }

    @Override
    public final void setType(Material material, boolean applyPhysics) {
        getBlock().setType(material, applyPhysics);
    }

    @Override
    public final boolean setTypeId(int id) {
        return getBlock().setTypeId(id);
    }

    @Override
    public final boolean setTypeId(int id, boolean applyPhysics) {
        return getBlock().setTypeId(id, applyPhysics);
    }

    @Override
    public final boolean setTypeIdAndData(int id, byte data, boolean applyPhysics) {
        return getBlock().setTypeIdAndData(id, data, applyPhysics);
    }

    @Override
    public final BlockFace getFace(Block block) {
        return getBlock().getFace(block);
    }

    @Override
    public BlockState getState() {
        return getBlock().getState();
    }

    @Override
    public final Biome getBiome() {
        return getBlock().getBiome();
    }

    @Override
    public final void setBiome(Biome biome) {
        getBlock().setBiome(biome);
    }

    @Override
    public final boolean isBlockPowered() {
        return getBlock().isBlockPowered();
    }

    @Override
    public final boolean isBlockIndirectlyPowered() {
        return getBlock().isBlockIndirectlyPowered();
    }

    @Override
    public final boolean isBlockFacePowered(BlockFace face) {
        return getBlock().isBlockFacePowered(face);
    }

    @Override
    public final boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        return getBlock().isBlockFaceIndirectlyPowered(face);
    }

    @Override
    public final int getBlockPower(BlockFace face) {
        return getBlock().getBlockPower(face);
    }

    @Override
    public final int getBlockPower() {
        return getBlock().getBlockPower();
    }

    @Override
    public final boolean isEmpty() {
        return getBlock().isEmpty();
    }

    @Override
    public final boolean isLiquid() {
        return getBlock().isLiquid();
    }

    @Override
    public final double getTemperature() {
        return getBlock().getTemperature();
    }

    @Override
    public final double getHumidity() {
        return getBlock().getHumidity();
    }

    @Override
    public final PistonMoveReaction getPistonMoveReaction() {
        return getBlock().getPistonMoveReaction();
    }

    @Override
    public final boolean breakNaturally() {
        return getBlock().breakNaturally();
    }

    @Override
    public final boolean breakNaturally(ItemStack stack) {
        return getBlock().breakNaturally(stack);
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return getBlock().getDrops();
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack itemStack) {
        return getBlock().getDrops(itemStack);
    }

    @Override
    public final void setMetadata(String key, MetadataValue value) {
        getBlock().setMetadata(key, value);
    }

    @Override
    public final List<MetadataValue> getMetadata(String key) {
        return getBlock().getMetadata(key);
    }

    @Override
    public final boolean hasMetadata(String key) {
        return getBlock().hasMetadata(key);
    }

    @Override
    public final void removeMetadata(String key, Plugin plugin) {
        getBlock().removeMetadata(key, plugin);
    }

}
