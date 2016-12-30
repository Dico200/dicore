package io.dico.dicore.nms.impl.V1_8_R3;

import io.dico.dicore.nms.NDriver;
import io.dico.dicore.nms.NItemStackSupport;
import io.dico.dicore.nms.NServer;

public class DriverImpl implements NDriver {
    private final NServer server = new ServerImpl();
    private final NItemStackSupport itemStackSupport = new ItemStackSupportImpl();

    @Override
    public NServer getServer() {
        return server;
    }

    @Override
    public NItemStackSupport getItemStackSupport() {
        return itemStackSupport;
    }

}
