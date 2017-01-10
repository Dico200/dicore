package io.dico.dicore.util.block;

import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.dico.dicore.saving.JsonLoadable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SerializableAs("BlockPos")
public abstract class CorePos<T extends CorePos<T> & Metadatable> implements
        Comparable<T>,
        Serializable,
        JsonLoadable,
        ConfigurationSerializable,
        Cloneable,
        Metadatable {
    private static final long serialVersionUID = 200L;
    protected static transient final Object unknown = new Object();
    protected String worldName;
    protected transient Object world = unknown;
    protected transient Object block = unknown;
    protected int x;
    protected int y;
    protected int z;
    protected boolean mutable;


    protected CorePos() {
    }

    protected CorePos(int x, int y, int z) {
        construct(x, y, z);
    }

    protected CorePos(String worldName, int x, int y, int z) {
        construct(worldName, x, y, z);
    }

    protected CorePos(World world, int x, int y, int z) {
        construct(world, x, y, z);
    }

    protected CorePos(JsonReader reader) throws IOException {
        loadFrom(reader);
    }

    protected CorePos(Map<String, Object> map) {
        construct(map);
    }

    protected CorePos(boolean mutable) {
        construct(mutable);
    }

    protected CorePos(boolean mutable, int x, int y, int z) {
        construct(mutable, x, y, z);
    }

    protected CorePos(boolean mutable, String worldName, int x, int y, int z) {
        construct(mutable, worldName, x, y, z);
    }

    protected CorePos(boolean mutable, World world, int x, int y, int z) {
        construct(mutable, world, x, y, z);
    }

    protected CorePos(boolean mutable, JsonReader reader) throws IOException {
        construct(mutable, reader);
    }

    protected CorePos(boolean mutable, Map<String, Object> map) {
        construct(mutable, map);
    }

    protected CorePos(Block block) {
        construct(block);
    }

    protected CorePos(BlockState block) {
        construct(block);
    }

    protected CorePos(Location loc) {
        construct(loc);
    }

    protected CorePos(boolean mutable, Block block) {
        construct(mutable, block);
    }

    protected CorePos(boolean mutable, BlockState block) {
        construct(mutable, block);
    }

    protected CorePos(boolean mutable, Location loc) {
        construct(mutable, loc);
    }

    protected abstract T createNew();

    protected abstract Metadatable getMetadatable();

    protected void construct(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected void construct(String worldName, int x, int y, int z) {
        construct(x, y, z);
        this.worldName = worldName;
    }

    protected void construct(World world, int x, int y, int z) {
        construct(Objects.requireNonNull(world).getName(), x, y, z);
        this.worldName = world.getName();
    }

    protected void construct(JsonReader reader) throws IOException {
        loadFrom(reader);
    }

    protected void construct(Map<String, Object> map) {
        worldName = cast(map.get("w"), null);
        x = cast(map.get("x"), 0);
        y = cast(map.get("y"), 0);
        z = cast(map.get("z"), 0);
        mutable = cast(map.get("m"), false);
    }

    protected void construct(boolean mutable) {
        this.mutable = mutable;
    }

    protected void construct(boolean mutable, int x, int y, int z) {
        construct(x, y, z);
        this.mutable = mutable;
    }

    protected void construct(boolean mutable, String worldName, int x, int y, int z) {
        construct(mutable, x, y, z);
        this.worldName = worldName;
    }

    protected void construct(boolean mutable, World world, int x, int y, int z) {
        construct(mutable, Objects.requireNonNull(world).getName(), x, y, z);
        this.world = world;
    }

    protected void construct(boolean mutable, JsonReader reader) throws IOException {
        loadFrom(reader);
        this.mutable = mutable;
    }

    protected void construct(boolean mutable, Map<String, Object> map) {
        construct(map);
        this.mutable = mutable;
    }

    protected void construct(Block block) {
        construct(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    protected void construct(BlockState block) {
        construct(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    protected void construct(Location loc) {
        construct(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    protected void construct(boolean mutable, Block block) {
        construct(block);
        this.mutable = mutable;
    }

    protected void construct(boolean mutable, BlockState block) {
        construct(block);
        this.mutable = mutable;
    }

    protected void construct(boolean mutable, Location loc) {
        construct(loc);
        this.mutable = mutable;
    }

    private T cast() {
        return (T) this;
    }


    public final String getWorldName() {
        return worldName;
    }

    public final World getWorld() {
        if (world == unknown) {
            world = worldName == null ? null : Bukkit.getWorld(worldName);
        }
        return (World) world;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getZ() {
        return z;
    }

    public final Block getBlock() {
        if (block == unknown) {
            World world = getWorld();
            block = world == null ? null : world.getBlockAt(x, y, z);
        }
        return (Block) block;
    }

    public T makeImmutable() {
        mutable = false;
        return (T) this;
    }

    public final boolean isMutable() {
        return mutable;
    }

    public T requireMutable() {
        if (!mutable) {
            throw new UnsupportedOperationException("This BlockPos is immutable");
        }
        return (T) this;
    }

    public T withX(int x) {
        if (this.x == x) {
            return (T) this;
        }
        T result = cloneIfImmutable();
        result.block = unknown;
        result.x = x;
        return result;
    }

    public T withY(int y) {
        if (this.y == y) {
            return (T) this;
        }
        T result = cloneIfImmutable();
        result.block = unknown;
        result.y = y;
        return result;
    }

    public T withZ(int z) {
        if (this.z == z) {
            return (T) this;
        }
        T result = cloneIfImmutable();
        result.block = unknown;
        result.z = z;
        return result;
    }

    public T with(int x, int y, int z) {
        if (this.x == x && this.y == y && this.z == z) {
            return (T) this;
        }
        T result = cloneIfImmutable();
        result.block = unknown;
        result.x = x;
        result.y = y;
        result.z = z;
        return result;
    }

    public T addX(int x) {
        return withX(this.x + x);
    }

    public T addY(int y) {
        return withY(this.y + y);
    }

    public T addZ(int z) {
        return withZ(this.z + z);
    }

    public T add(int x, int y, int z) {
        return with(this.x + x, this.y + y, this.z + z);
    }

    public T add(T other) {
        return add(other.x, other.y, other.z);
    }

    public T subtract(T other) {
        return add(-other.x, -other.y, -other.z);
    }

    public T multiplyX(int x) {
        return withX(this.x * x);
    }

    public T multiplyY(int y) {
        return withY(this.y * y);
    }

    public T multiplyZ(int z) {
        return withZ(this.z * z);
    }

    public T multiply(int x, int y, int z) {
        return with(this.x * x, this.y * y, this.z * z);
    }

    public T multiplyX(double x) {
        return withX((int) (this.x * x));
    }

    public T multiplyY(double y) {
        return withY((int) (this.y * y));
    }

    public T multiplyZ(double z) {
        return withZ((int) (this.z * z));
    }

    public T multiply(double x, double y, double z) {
        return with((int) (this.x * x), (int) (this.y * y), (int) (this.z * z));
    }

    public T divideX(int x) {
        return withX(this.x / x);
    }

    public T divideY(int y) {
        return withY(this.y / y);
    }

    public T divideZ(int z) {
        return withZ(this.z / z);
    }

    public T divide(int x, int y, int z) {
        return with(this.x / x, this.y / y, this.z / z);
    }

    public T divideX(double x) {
        return withX((int) (this.x / x));
    }

    public T divideY(double y) {
        return withY((int) (this.y / y));
    }

    public T divideZ(double z) {
        return withZ((int) (this.z / z));
    }

    public T divide(double x, double y, double z) {
        return with((int) (this.x / x), (int) (this.y / y), (int) (this.z / z));
    }

    public T moduloX(int x) {
        return withX(this.x % x);
    }

    public T moduloY(int y) {
        return withY(this.y % y);
    }

    public T moduloZ(int z) {
        return withZ(this.z % z);
    }

    public T modulo(int x, int y, int z) {
        return with(this.x % x, this.y % y, this.z % z);
    }

    public T moduloX(double x) {
        return withX((int) (this.x % x));
    }

    public T moduloY(double y) {
        return withY((int) (this.y % y));
    }

    public T moduloZ(double z) {
        return withZ((int) (this.z % z));
    }

    public T modulo(double x, double y, double z) {
        return with((int) (this.x % x), (int) (this.y % y), (int) (this.z % z));
    }

    public T withWorld(World world) {
        if (this.world == world) {
            return (T) this;
        }
        T result = cloneIfImmutable();
        result.block = unknown;
        result.world = world;
        result.worldName = world.getName();
        return result;
    }

    public final Chunk getChunk() {
        return getWorld().getChunkAt(x >> 4, z >> 4);
    }

    public final Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    public final Location getLocation(Location loc) {
        if(loc != null) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        //TODO make equal if block equal
        if (o == null || getClass() != o.getClass()) return false;

        T that = (T) o;

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

    public final String pos() {
        return String.format("%s(%d,%d,%d)", worldName, x, y, z);
    }

    public T cloneIfImmutable() {
        return mutable ? (T) this : clone();
    }

    public T cloneIfMutable() {
        return mutable ? clone() : (T) this;
    }

    @Override
    public T clone() {
        T result;
        try {
            result = (T) super.clone();
        } catch (CloneNotSupportedException ex) {
            result = createNew();
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
    public int compareTo(T o) {
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

    public static CorePos deserialize(Map<String, Object> map) {
        CorePos result = createBare();
        result.construct(map);
        return result;
    }

    private static CorePos createBare() {
        return new CorePos() {
            @Override
            protected CorePos createNew() {
                return createBare();
            }

            @Override
            protected Metadatable getMetadatable() {
                return null;
            }
        };
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
        ConfigurationSerialization.registerClass(CorePos.class);
    }

    @Override
    public void setMetadata(String key, MetadataValue metadataValue) {
        getMetadatable().setMetadata(key, metadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String key) {
        return getMetadatable().getMetadata(key);
    }

    @Override
    public boolean hasMetadata(String key) {
        return getMetadatable().hasMetadata(key);
    }

    @Override
    public void removeMetadata(String key, Plugin plugin) {
        getMetadatable().removeMetadata(key, plugin);
    }

}
