package io.dico.dicore.nms;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.UUID;

public interface NWorld {

    Entity getEntity(UUID uuid);

    Map<UUID, Entity> getEntitiesByUUID();

    void playBlockAction(Material blockType, int x, int y, int z, int tag);

    void applyPhysics(int x, int y, int z, Material block);

    void showParticle(Particle type, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int id, int particleCount, double radius);

}
