package io.dico.dicore.nms;

import io.dico.dicore.nms.impl.V1_8_R3.DriverImpl;
import org.bukkit.Bukkit;

public interface NDriver {

    static NDriver getInstance() {
        return Version.getDriver();
    }

    NServer getServer();

    NItemStackSupport getItemStackSupport();

    enum Version {

        V1_8_R3;

        private static final Version instance;
        private static final NDriver driver;

        public static Version getInstance() {
            if (instance == null) {
                throw new IllegalStateException();
            }
            return instance;
        }

        public static NDriver getDriver() {
            if (driver == null) {
                throw new IllegalStateException();
            }
            return driver;
        }

        static {
            String serverClass = Bukkit.getServer().getClass().getName();
            String[] split = serverClass.split("\\.");
            if (split.length >= 2) {
                instance = valueOf(split[split.length - 2]);

                switch (instance) {
                    case V1_8_R3:
                        driver = new DriverImpl();
                        break;
                    default:
                        driver = null;
                }

            } else {
                throw new IllegalStateException();
            }
        }

    }

}
