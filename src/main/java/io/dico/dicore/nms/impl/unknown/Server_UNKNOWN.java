package io.dico.dicore.nms.impl.unknown;

import io.dico.dicore.nms.NProperties;
import io.dico.dicore.nms.NServer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

class Server_UNKNOWN implements NServer {
    NProperties props = new Properties_UNKNOWN();
    Configuration bukkitConfig = new YamlConfiguration();
    Configuration commandConfig = new YamlConfiguration();

    @Override
    public int getCurrentTick() {
        return 0;
    }

    @Override
    public void setTickSpeed(int tickSpeed) {

    }

    @Override
    public int getTickSpeed() {
        return 20;
    }

    @Override
    public NProperties getProperties() {
        return props;
    }

    @Override
    public void setMotd(String motd) {

    }

    @Override
    public String getMotd() {
        return "UNKNOWN";
    }

    @Override
    public boolean playerCommandState() {
        return false;
    }

    @Override
    public Configuration getBukkitConfig() {
        return bukkitConfig;
    }

    @Override
    public Configuration getCommandConfig() {
        return commandConfig;
    }

}
