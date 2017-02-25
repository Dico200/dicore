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

}
