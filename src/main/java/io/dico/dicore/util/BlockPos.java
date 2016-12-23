package io.dico.dicore.util;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.io.IOException;

public class BlockPos implements Comparable<BlockPos> {

    private final String worldName;
    private final World world;
    private final int x;
    private final int y;
    private final int z;

    public BlockPos(JsonReader reader) throws IOException {
        String worldName = null;
        Integer x = null;
        Integer y = null;
        Integer z = null;

        reader.beginObject();
        while (reader.hasNext()) {
            final String key = reader.nextName();
            switch (key) {
                case "w":
                    worldName = reader.nextString();
                    break;
                case "x":
                    x = reader.nextInt();
                    break;
                case "y":
                    y = reader.nextInt();
                    break;
                case "z":
                    z = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        this.world = worldName == null ? null : Bukkit.getWorld(worldName);
        this.worldName = worldName;
        this.x = x == null ? 0 : x;
        this.y = y == null ? 0 : y;
        this.z = z == null ? 0 : z;
    }

    public BlockPos(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.world = Bukkit.getWorld(worldName);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(Block block) {
        this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
    }

    public BlockPos(BlockState block) {
        this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
    }

    public String getWorldName() {
        return worldName;
    }

    public World getWorld() {
        return world;
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
        if (world == null) return null;
        return world.getBlockAt(x, y, z);
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

    @Override
    public int compareTo(BlockPos o) {
        return o.worldName.hashCode() - worldName.hashCode();
    }

    public void writeTo(JsonWriter writer) throws IOException {

        writer.beginObject();
        writer.name("w").value(worldName);
        writer.name("x").value(x);
        writer.name("y").value(y);
        writer.name("z").value(z);
        writer.endObject();
    }
}
