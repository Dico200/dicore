package io.dico.dicore.util;

import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.JsonLoadable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@SerializableAs("BlockPos")
public final class BlockPos implements Comparable<BlockPos>, Serializable, JsonLoadable, ConfigurationSerializable, Cloneable {
    private static final long serialVersionUID = 200L;
    private static transient final Object unknown = new Object();
    private String worldName;
    private transient Object world = unknown;
    private transient Object block = unknown;
    private int x;
    private int y;
    private int z;
    private boolean mutable;

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
        if (o == null || getClass() != o.getClass()) return false;

        BlockPos that = (BlockPos) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        return worldName.equals(that.worldName);
    }

    @Override
    public int hashCode() {
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
            final String key = reader.nextName();
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

}
