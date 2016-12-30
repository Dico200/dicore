package io.dico.dicore.nms;

import org.bukkit.configuration.Configuration;

public interface NServer {

    int getCurrentTick();

    void setTickSpeed(int tickSpeed);

    int getTickSpeed();

    NProperties getProperties();

    void setMotd(String motd);

    String getMotd();

    boolean playerCommandState();

    Configuration getBukkitConfig();

    Configuration getCommandConfig();

}
