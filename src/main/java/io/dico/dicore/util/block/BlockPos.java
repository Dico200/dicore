package io.dico.dicore.util.block;

import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.JsonLoadable;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SerializableAs("BlockPos")
public final class BlockPos implements Block, Comparable<BlockPos>, Serializable, JsonLoadable, ConfigurationSerializable, Cloneable, Externalizable {
    private static final long serialVersionUID = 200L;
    protected static final transient Object unknown = new Object();
    protected String worldName;
    protected transient Object world = unknown;
    protected transient Object block = unknown;
    protected int x;
    protected int y;
    protected int z;
    protected boolean mutable;
    
    public BlockPos() {
    }
    
    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public BlockPos(String worldName, int x, int y, int z) {
        this(x, y, z);
        this.worldName = worldName;
    }
    
    public BlockPos(World world, int x, int y, int z) {
        this(Objects.requireNonNull(world).getName(), x, y, z);
        this.worldName = world.getName();
    }
    
    public BlockPos(JsonReader reader) throws IOException {
        loadFrom(reader);
    }
    
    public BlockPos(Map<String, Object> map) {
        worldName = cast(map.get("w"), null);
        x = cast(map.get("x"), 0);
        y = cast(map.get("y"), 0);
        z = cast(map.get("z"), 0);
        mutable = cast(map.get("m"), false);
    }
    
    public BlockPos(boolean mutable) {
        this.mutable = mutable;
    }
    
    public BlockPos(boolean mutable, int x, int y, int z) {
        this(x, y, z);
        this.mutable = mutable;
    }
    
    public BlockPos(boolean mutable, String worldName, int x, int y, int z) {
        this(mutable, x, y, z);
        this.worldName = worldName;
    }
    
    public BlockPos(boolean mutable, World world, int x, int y, int z) {
        this(mutable, Objects.requireNonNull(world).getName(), x, y, z);
        this.world = world;
    }
    
    public BlockPos(boolean mutable, JsonReader reader) throws IOException {
        loadFrom(reader);
        this.mutable = mutable;
    }
    
    public BlockPos(boolean mutable, Map<String, Object> map) {
        this(map);
        this.mutable = mutable;
    }
    
    public BlockPos(Block block) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }
    
    public BlockPos(BlockState block) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }
    
    public BlockPos(Location loc) {
        this(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
    
    public BlockPos(boolean mutable, Block block) {
        this(block);
        this.mutable = mutable;
    }
    
    public BlockPos(boolean mutable, BlockState block) {
        this(block);
        this.mutable = mutable;
    }
    
    public BlockPos(boolean mutable, Location loc) {
        this(loc);
        this.mutable = mutable;
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public World getWorld() {
        if (world == unknown) {
            world = worldName == null ? null : Bukkit.getWorld(worldName);
        }
        return (World) world;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public Block getBlock() {
        if (block == unknown) {
            World world = getWorld();
            block = world == null ? null : world.getBlockAt(x, y, z);
        }
        return (Block) block;
    }
    
    public BlockPos makeImmutable() {
        mutable = false;
        return this;
    }
    
    public boolean isMutable() {
        return mutable;
    }
    
    public BlockPos requireMutable() {
        if (!mutable) {
            throw new UnsupportedOperationException("This BlockPos is immutable");
        }
        return this;
    }
    
    public BlockPos withX(int x) {
        if (this.x == x) {
            return this;
        }
        BlockPos result = cloneIfImmutable();
        result.block = unknown;
        result.x = x;
        return result;
    }
    
    public BlockPos withY(int y) {
        if (this.y == y) {
            return this;
        }
        BlockPos result = cloneIfImmutable();
        result.block = unknown;
        result.y = y;
        return result;
    }
    
    public BlockPos withZ(int z) {
        if (this.z == z) {
            return this;
        }
        BlockPos result = cloneIfImmutable();
        result.block = unknown;
        result.z = z;
        return result;
    }
    
    public BlockPos with(int x, int y, int z) {
        if (this.x == x && this.y == y && this.z == z) {
            return this;
        }
        BlockPos result = cloneIfImmutable();
        result.block = unknown;
        result.x = x;
        result.y = y;
        result.z = z;
        return result;
    }
    
    public BlockPos addX(int x) {
        return withX(this.x + x);
    }
    
    public BlockPos addY(int y) {
        return withY(this.y + y);
    }
    
    public BlockPos addZ(int z) {
        return withZ(this.z + z);
    }
    
    public BlockPos add(int x, int y, int z) {
        return with(this.x + x, this.y + y, this.z + z);
    }
    
    public BlockPos add(BlockPos other) {
        return add(other.x, other.y, other.z);
    }
    
    public BlockPos subtract(BlockPos other) {
        return add(-other.x, -other.y, -other.z);
    }
    
    public BlockPos multiplyX(int x) {
        return withX(this.x * x);
    }
    
    public BlockPos multiplyY(int y) {
        return withY(this.y * y);
    }
    
    public BlockPos multiplyZ(int z) {
        return withZ(this.z * z);
    }
    
    public BlockPos multiply(int x, int y, int z) {
        return with(this.x * x, this.y * y, this.z * z);
    }
    
    public BlockPos multiplyX(double x) {
        return withX((int) (this.x * x));
    }
    
    public BlockPos multiplyY(double y) {
        return withY((int) (this.y * y));
    }
    
    public BlockPos multiplyZ(double z) {
        return withZ((int) (this.z * z));
    }
    
    public BlockPos multiply(double x, double y, double z) {
        return with((int) (this.x * x), (int) (this.y * y), (int) (this.z * z));
    }
    
    public BlockPos divideX(int x) {
        return withX(this.x / x);
    }
    
    public BlockPos divideY(int y) {
        return withY(this.y / y);
    }
    
    public BlockPos divideZ(int z) {
        return withZ(this.z / z);
    }
    
    public BlockPos divide(int x, int y, int z) {
        return with(this.x / x, this.y / y, this.z / z);
    }
    
    public BlockPos divideX(double x) {
        return withX((int) (this.x / x));
    }
    
    public BlockPos divideY(double y) {
        return withY((int) (this.y / y));
    }
    
    public BlockPos divideZ(double z) {
        return withZ((int) (this.z / z));
    }
    
    public BlockPos divide(double x, double y, double z) {
        return with((int) (this.x / x), (int) (this.y / y), (int) (this.z / z));
    }
    
    public BlockPos moduloX(int x) {
        return withX(this.x % x);
    }
    
    public BlockPos moduloY(int y) {
        return withY(this.y % y);
    }
    
    public BlockPos moduloZ(int z) {
        return withZ(this.z % z);
    }
    
    public BlockPos modulo(int x, int y, int z) {
        return with(this.x % x, this.y % y, this.z % z);
    }
    
    public BlockPos moduloX(double x) {
        return withX((int) (this.x % x));
    }
    
    public BlockPos moduloY(double y) {
        return withY((int) (this.y % y));
    }
    
    public BlockPos moduloZ(double z) {
        return withZ((int) (this.z % z));
    }
    
    public BlockPos modulo(double x, double y, double z) {
        return with((int) (this.x % x), (int) (this.y % y), (int) (this.z % z));
    }
    
    public BlockPos withWorld(World world) {
        if (this.world == world) {
            return this;
        }
        BlockPos result = cloneIfImmutable();
        result.block = unknown;
        result.world = world;
        result.worldName = world.getName();
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //TODO make equal if block equal
        if (o == null || getClass() != o.getClass()) return false;
        
        BlockPos that = (BlockPos) o;
        
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        return worldName.equals(that.worldName);
    }
    
    @Override
    public int hashCode() {
        //TODO use same algorithm as CraftBlock
        int result = worldName.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
    
    @Override
    public String toString() {
        return "BlockPos{" +
                "worldName='" + worldName + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
    
    public String pos() {
        return String.format("%s(%d,%d,%d)", worldName, x, y, z);
    }
    
    public BlockPos cloneIfImmutable() {
        return mutable ? this : clone();
    }
    
    public BlockPos cloneIfMutable() {
        return mutable ? clone() : this;
    }
    
    @Override
    public BlockPos clone() {
        BlockPos result;
        try {
            result = (BlockPos) super.clone();
        } catch (CloneNotSupportedException ex) {
            result = new BlockPos();
            result.worldName = worldName;
            result.world = world;
            result.block = block;
            result.x = x;
            result.y = y;
            result.z = z;
        }
        result.mutable = true;
        return result;
    }
    
    @Override
    public int compareTo(BlockPos o) {
        return o.worldName.hashCode() - worldName.hashCode();
    }
    
    @Override
    public void loadFrom(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (!key.isEmpty()) switch (key.charAt(0)) {
                case 'w':
                    worldName = reader.nextString();
                    break;
                case 'x':
                    x = reader.nextInt();
                    break;
                case 'y':
                    y = reader.nextInt();
                    break;
                case 'z':
                    z = reader.nextInt();
                    break;
                case 'm':
                    mutable = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
    }
    
    @Override
    public void writeTo(JsonWriter writer) throws IOException {
        writer.beginObject();
        if (worldName != null) {
            writer.name("w").value(worldName);
        }
        if (x != 0) {
            writer.name("x").value(x);
        }
        if (y != 0) {
            writer.name("y").value(y);
        }
        if (z != 0) {
            writer.name("z").value(z);
        }
        if (mutable) {
            writer.name("m").value(true);
        }
        writer.endObject();
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(worldName);
        out.write(x);
        out.write(y);
        out.write(z);
        out.writeByte((byte) (mutable ? 1 : 0));
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        world = in.readUTF();
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        mutable = in.readByte() == 1;
    }
    
    private static <T> T cast(Object o, T def) {
        try {
            return (T) o;
        } catch (ClassCastException e) {
            return def;
        }
    }
    
    public static BlockPos deserialize(Map<String, Object> map) {
        return new BlockPos(map);
    }
    
    @Override
    public Map<String, Object> serialize() {
        ImmutableMap.Builder<String, Object> result = new ImmutableMap.Builder<>();
        if (worldName != null) {
            result.put("w", worldName);
        }
        if (x != 0) {
            result.put("x", x);
        }
        if (y != 0) {
            result.put("y", y);
        }
        if (z != 0) {
            result.put("z", z);
        }
        if (mutable) {
            result.put("m", true);
        }
        return result.build();
    }
    
    static {
        ConfigurationSerialization.registerClass(BlockPos.class);
    }
    
    @Override
    public byte getData() {
        return getBlock().getData();
    }
    
    @Override
    public BlockPos getRelative(int x, int y, int z) {
        return add(x, y, z);
    }
    
    @Override
    public BlockPos getRelative(BlockFace face) {
        return add(face.getModX(), face.getModY(), face.getModZ());
    }
    
    @Override
    public BlockPos getRelative(BlockFace face, int multiplier) {
        return add(face.getModX() * multiplier, face.getModX() * multiplier, face.getModZ() * multiplier);
    }
    
    @Override
    public Material getType() {
        return getBlock().getType();
    }
    
    @Override
    public int getTypeId() {
        return getBlock().getTypeId();
    }
    
    @Override
    public Chunk getChunk() {
        return getWorld().getChunkAt(x >> 4, z >> 4);
    }
    
    @Override
    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }
    
    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(x);
            loc.setY(y);
            loc.setZ(z);
            loc.setYaw(0);
            loc.setPitch(0);
        }
        return loc;
    }
    
    @Override
    public byte getLightLevel() {
        return getBlock().getLightLevel();
    }
    
    @Override
    public byte getLightFromSky() {
        return getBlock().getLightFromSky();
    }
    
    @Override
    public byte getLightFromBlocks() {
        return getBlock().getLightFromBlocks();
    }
    
    @Override
    public void setData(byte data) {
        getBlock().setData(data);
    }
    
    @Override
    public void setData(byte data, boolean applyPhysics) {
        getBlock().setData(data, applyPhysics);
    }
    
    @Override
    public void setType(Material material) {
        getBlock().setType(material);
    }
    
    @Override
    public void setType(Material material, boolean applyPhysics) {
        getBlock().setType(material, applyPhysics);
    }
    
    @Override
    public boolean setTypeId(int id) {
        return getBlock().setTypeId(id);
    }
    
    @Override
    public boolean setTypeId(int id, boolean applyPhysics) {
        return getBlock().setTypeId(id, applyPhysics);
    }
    
    @Override
    public boolean setTypeIdAndData(int id, byte data, boolean applyPhysics) {
        return getBlock().setTypeIdAndData(id, data, applyPhysics);
    }
    
    @Override
    public BlockFace getFace(Block block) {
        return getBlock().getFace(block);
    }
    
    @Override
    public BlockState getState() {
        return getBlock().getState();
    }
    
    @Override
    public Biome getBiome() {
        return getBlock().getBiome();
    }
    
    @Override
    public void setBiome(Biome biome) {
        getBlock().setBiome(biome);
    }
    
    @Override
    public boolean isBlockPowered() {
        return getBlock().isBlockPowered();
    }
    
    @Override
    public boolean isBlockIndirectlyPowered() {
        return getBlock().isBlockIndirectlyPowered();
    }
    
    @Override
    public boolean isBlockFacePowered(BlockFace face) {
        return getBlock().isBlockFacePowered(face);
    }
    
    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        return getBlock().isBlockFaceIndirectlyPowered(face);
    }
    
    @Override
    public int getBlockPower(BlockFace face) {
        return getBlock().getBlockPower(face);
    }
    
    @Override
    public int getBlockPower() {
        return getBlock().getBlockPower();
    }
    
    @Override
    public boolean isEmpty() {
        return getBlock().isEmpty();
    }
    
    @Override
    public boolean isLiquid() {
        return getBlock().isLiquid();
    }
    
    @Override
    public double getTemperature() {
        return getBlock().getTemperature();
    }
    
    @Override
    public double getHumidity() {
        return getBlock().getHumidity();
    }
    
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return getBlock().getPistonMoveReaction();
    }
    
    @Override
    public boolean breakNaturally() {
        return getBlock().breakNaturally();
    }
    
    @Override
    public boolean breakNaturally(ItemStack stack) {
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
    public void setMetadata(String key, MetadataValue value) {
        getBlock().setMetadata(key, value);
    }
    
    @Override
    public List<MetadataValue> getMetadata(String key) {
        return getBlock().getMetadata(key);
    }
    
    @Override
    public boolean hasMetadata(String key) {
        return getBlock().hasMetadata(key);
    }
    
    @Override
    public void removeMetadata(String key, Plugin plugin) {
        getBlock().removeMetadata(key, plugin);
    }
    
}
