package io.dico.dicore.nms.impl.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.nms.Particle;
import io.dico.dicore.util.Reflection;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;


import java.util.List;
import java.util.Map;
import java.util.UUID;

class WorldImpl implements NWorld {
    private static final EnumParticle[] particleValues = EnumParticle.values();
    private static final Predicate<Entity> playerPredicate = EntityPlayer.class::isInstance;
    private final WorldServer server;
    private Map<UUID, org.bukkit.entity.Entity> entitiesByUUID;

    public WorldImpl(WorldServer server) {
        this.server = server;
    }

    public WorldImpl(World world) {
        this(((CraftWorld) world).getHandle());
    }

    @Override
    public org.bukkit.entity.Entity getEntity(UUID uuid) {
        Entity result = server.getEntity(uuid);
        return result == null ? null : result.getBukkitEntity();
    }

    @Override
    public Map<UUID, org.bukkit.entity.Entity> getEntitiesByUUID() {
        if (entitiesByUUID == null) {
            Map<UUID, Entity> map = Reflection.getValueInField(WorldServer.class, "entitiesByUUID", server);
            entitiesByUUID = Maps.transformValues(map, Entity::getBukkitEntity);
        }
        return entitiesByUUID;
    }
    
    @Override
    public void playBlockAction(Material blockType, int x, int y, int z, int tag) {
        Block block = Block.REGISTRY.a(blockType.getId());
        block.a(server, new BlockPosition(x, y, z), block.getBlockData(), 1, tag);
    }
    
    @Override
    public void applyPhysics(int x, int y, int z, Material block) {
        server.applyPhysics(new BlockPosition(x, y, z), Block.REGISTRY.a(block.getId()));
    }
/*
    public void showParticle(Particle type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, )
*/
    @Override
    public void showParticle(Particle type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int id, int particleCount, double radius) {
        EnumParticle particle = particleValues[type.ordinal()];
        Packet packet = new PacketPlayOutWorldParticles(particle, true, x, y, z, offsetX, offsetY, offsetZ, speed, particleCount, id);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);

        List<Entity> entities = server.a((Entity) null, axisAlignedBB, playerPredicate);

        for (Entity entity : entities) {
            EntityPlayer player = (EntityPlayer) entity;
            player.playerConnection.sendPacket(packet);
        }

    }

}
