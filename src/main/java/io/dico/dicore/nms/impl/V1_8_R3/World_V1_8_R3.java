package io.dico.dicore.nms.impl.V1_8_R3;

import com.google.common.collect.Maps;
import io.dico.dicore.nms.NWorld;
import io.dico.dicore.util.Reflection;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.UUID;

public class World_V1_8_R3 implements NWorld {

    private final WorldServer server;
    private Map<UUID, Entity> entitiesByUUID;

    public World_V1_8_R3(WorldServer server) {
        this.server = server;
    }

    public World_V1_8_R3(World world) {
        this(((CraftWorld) world).getHandle());
    }

    @Override
    public Entity getEntity(UUID uuid) {
        net.minecraft.server.v1_8_R3.Entity result = server.getEntity(uuid);
        return result == null ? null : result.getBukkitEntity();
    }

    @Override
    public Map<UUID, Entity> getEntitiesByUUID() {
        if (entitiesByUUID == null) {
            Map<UUID, net.minecraft.server.v1_8_R3.Entity> map = Reflection.getValueInField(WorldServer.class, "entitiesByUUID", server);
            entitiesByUUID = Maps.transformValues(map, net.minecraft.server.v1_8_R3.Entity::getBukkitEntity);
        }
        return entitiesByUUID;
    }

}
