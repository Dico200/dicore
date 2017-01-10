package io.dico.dicore.nms.impl.V1_8_R3;

import io.dico.dicore.nms.NProperties;
import io.dico.dicore.nms.NServer;
import io.dico.dicore.util.Reflection;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

class Server_V1_8_R3 implements NServer {
    private final DedicatedServer mserver = (DedicatedServer) MinecraftServer.getServer();
    private final CraftServer bserver = mserver.server;
    private final NProperties properties = new Properties_V1_8_R3(mserver.propertyManager);

    @Override
    public int getCurrentTick() {
        return MinecraftServer.currentTick;
    }

    @Override
    public void setTickSpeed(int tickSpeed) {
        Reflection.setValueInFinalField(Reflection.getField(MinecraftServer.class, "TPS"), mserver, tickSpeed);
    }

    @Override
    public int getTickSpeed() {
        return Reflection.getValueInField(Reflection.getField(MinecraftServer.class, "TPS"), mserver);
    }

    public NProperties getProperties() {
        return properties;
    }

    @Override
    public void setMotd(String motd) {
        mserver.setMotd(motd);
    }

    @Override
    public String getMotd() {
        return mserver.getMotd();
    }

    @Override
    public boolean playerCommandState() {
        return bserver.playerCommandState;
    }

    @Override
    public Configuration getBukkitConfig() {
        return Reflection.getValueInField(bserver, "configuration");
    }

    @Override
    public Configuration getCommandConfig() {
        return Reflection.getValueInField(bserver, "commandsConfiguration");
    }
}
