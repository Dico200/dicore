package io.dico.dicore.nms.impl.unknown;

import io.dico.dicore.nms.NWorld;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class World_UNKNOWN implements NWorld {

    @Override
    public Entity getEntity(UUID uuid) {
        return null;
    }

    @Override
    public Map<UUID, Entity> getEntitiesByUUID() {
        return new HashMap<>(0);
    }

}
