package io.dico.dicore.util;

import java.util.logging.Logger;

public interface Logging {
    
    void info(Object o);
    
    void warn(Object o);
    
    void error(Object o);
    
    void debug(Object o);
    
    void setDebugging(boolean debugging);
    
    boolean isDebugging();
    
    class RootLogging implements Logging {
        private final String prefix;
        private final Logger root;
        private boolean debugging;
        
        public RootLogging(String prefix, Logger root, boolean debugging) {
            this.root = root;
            this.prefix = prefix;
            this.debugging = debugging;
        }
        
        @Override
        public void info(Object o) {
            root.info(prefix(o));
        }
        
        @Override
        public void warn(Object o) {
            root.warning(prefix(o));
        }
        
        @Override
        public void error(Object o) {
            root.severe(prefix(o));
        }
        
        @Override
        public void debug(Object o) {
            if (debugging) {
                root.info(String.format("[DEBUG] %s", prefix(o)));
            }
        }
        
        @Override
        public boolean isDebugging() {
            return debugging;
        }
        
        @Override
        public void setDebugging(boolean debugging) {
            this.debugging = debugging;
        }
        
        private String prefix(Object o) {
            return String.format("[%s] %s", prefix, String.valueOf(o));
        }
    }
    
    class SubLogging implements Logging {
        private final String prefix;
        private final Logging superLogger;
        private boolean debugging;
        
        public SubLogging(String prefix, Logging superLogger, boolean debugging) {
            this.superLogger = superLogger;
            this.prefix = prefix;
            this.debugging = debugging;
        }
        
        @Override
        public void info(Object o) {
            superLogger.info(prefix(o));
        }
        
        @Override
        public void warn(Object o) {
            superLogger.warn(prefix(o));
        }
        
        @Override
        public void error(Object o) {
            superLogger.error(prefix(o));
        }
        
        @Override
        public void debug(Object o) {
            if (debugging) {
                superLogger.info(String.format("[DEBUG] %s", prefix(o)));
            }
        }
        
        @Override
        public boolean isDebugging() {
            return debugging;
        }
        
        @Override
        public void setDebugging(boolean debugging) {
            this.debugging = debugging;
        }
        
        private String prefix(Object o) {
            return String.format("[%s] %s", prefix, String.valueOf(o));
        }
        
    }
    
}
