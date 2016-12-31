package io.dico.dicore.nms;

import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.UUID;

public interface NWorld {

    Entity getEntity(UUID uuid);

    Map<UUID, Entity> getEntitiesByUUID();

}
