package io.dico.dicore.nms.impl.v1_7_R4;

import com.google.common.collect.Maps;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.nms.Particle;
import io.dico.dicore.util.Reflection;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

class WorldImpl implements NWorld {
    private static final IEntitySelector playerPredicate = EntityPlayer.class::isInstance;
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
        Objects.requireNonNull(uuid);
        Entity result = null;
        
        for (Object entity : server.entityList) {
            if (uuid.equals(((Entity) entity).getUniqueID())) {
                return ((Entity) entity).getBukkitEntity();
            }
        }
        
        return null;
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
        Block block = (Block) Block.REGISTRY.a(blockType.getId());
        block.a(server, x, y, z, 1, tag);
    }
    
    @Override
    public void applyPhysics(int x, int y, int z, Material block) {
        server.applyPhysics(x, y, z, (Block) Block.REGISTRY.a(block.getId()));
    }
    
    /*
        public void showParticle(Particle type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, )
    */
    @Override
    public void showParticle(Particle type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int id, int particleCount, double radius) {
        String particle = type.name();
        Packet packet = new PacketPlayOutWorldParticles(particle, x, y, z, offsetX, offsetY, offsetZ, speed, particleCount);
        AxisAlignedBB axisAlignedBB = AxisAlignedBB.a(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        
        List entities = server.getEntities(null, axisAlignedBB, playerPredicate);
            
        for (Object entity : entities) {
            EntityPlayer player = (EntityPlayer) entity;
            player.playerConnection.sendPacket(packet);
        }
        
    }
    
}
