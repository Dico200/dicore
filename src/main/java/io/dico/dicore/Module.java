package io.dico.dicore;

import io.dico.dicore.util.Logging;
import io.dico.dicore.util.Registrator;
import io.dico.dicore.util.exceptions.ExceptionHandler;
import io.dico.dicore.util.exceptions.Exceptions;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.*;
import java.util.Objects;

public class Module<Manager extends ModuleManager> extends Logging.SubLogging {
    private final Manager manager;
    private final String name;
    private final boolean usesConfig;
    private final String baseFilename;
    private FileConfiguration config;
    private boolean enabled;
    private boolean enabledBefore;
    
    protected Module(String name, Manager manager, boolean usesConfig, boolean debugging) {
        super(name, manager, debugging);
        this.manager = Objects.requireNonNull(manager);
        this.name = name;
        this.usesConfig = usesConfig;
        baseFilename = name.toLowerCase().replace(" ", "_");
    }
    
    protected void load() {
        
    }
    
    protected void enable() {
        
    }
    
    protected void disable() {
        
    }
    
    protected void update() {
        
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public Manager getManager() {
        return manager;
    }
    
    public Registrator getRegistrator() {
        return manager.getRegistrator();
    }
    
    void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        
        if (enabled) {
            this.enabled = true;
            
            if (!enabledBefore) {
                enabledBefore = true;
                try {
                    load();
                } catch (Exception ex) {
                    ExceptionHandler.log(this::error, "loading", ex);
                }
            }
            
            try {
                enable();
                info("enabled successfully");
            } catch (Exception ex) {
                ExceptionHandler.log(this::error, "enabling", ex);
            }
    
            if (this instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) this, manager.getPlugin());
            }
            
        } else {
            try {
                disable();
            } catch (Exception ex) {
                ExceptionHandler.log(this::error, "disabling", ex);
            }
            
            this.enabled = false;
            
            if (this instanceof Listener) {
                HandlerList.unregisterAll((Listener) this);
            }
        }
    }
    
    private void checkUsesConfig() {
        if (!usesConfig) {
            throw new UnsupportedOperationException("This module does not use config files.");
        }
    }
    
    public FileConfiguration getConfig() {
        checkUsesConfig();
        if (config == null) {
            reloadConfig();
        }
        return config;
    }
    
    public File getDataFolder() {
        File result = new File(manager.getDataFolder(), baseFilename);
        if (!result.exists()) {
            Exceptions.runSafe(result::mkdirs);
        }
        return result;
    }
    
    private File getConfigFile() {
        checkUsesConfig();
        return new File(getDataFolder(), "config.yml");
    }
    
    private InputStream getDefaultConfigFile() {
        InputStream stream = Module.class.getResourceAsStream("/" + baseFilename + "-config.yml");
        
        if (stream == null) {
            debug("Didn't find default config");
            return null;
        }
        
        return stream;
    }
    
    public void reloadConfig() {
        checkUsesConfig();
        
        try (InputStream stream = new FileInputStream(getConfigFile())) {
            config = loadYaml(stream, "config");
        } catch (IOException ex) {
            if (ex instanceof FileNotFoundException) {
                
                try (InputStream in = getDefaultConfigFile();
                     OutputStream out = new FileOutputStream(getConfigFile())) {
                    if (in != null) {
                        IOUtils.copy(in, out);
                    }
                } catch (IOException ex2) {
                    ExceptionHandler.log(this::error, "writing default config", ex2);
                }
                
            } else {
                ExceptionHandler.log(this::error, "loading config", ex);
            }
            
            if (config == null) {
                config = new YamlConfiguration();
            }
        }
        
        try (InputStream stream = getDefaultConfigFile()) {
            if (stream != null) {
                Configuration defaults = loadYaml(stream, "default config");
                config.setDefaults(defaults);
                config.options().copyDefaults(true);
            }
        } catch (IOException ex) {
            ExceptionHandler.log(this::error, "loading default config", ex);
        }
        
    }
    
    private YamlConfiguration loadYaml(InputStream config, String configType) {
        YamlConfiguration result = new YamlConfiguration();
        if (config != null) try {
            String contents = toString(config, "Failed to load " + configType + ", cause unknown");
            result.loadFromString(contents);
        } catch (InvalidConfigurationException e) {
            error("Failed to load " + configType + ", it is of invalid syntax.");
            e.printStackTrace();
        }
        return result;
    }
    
    private String toString(InputStream stream, String exceptionMessage) {
        StringBuilder retBuilder = new StringBuilder();
        try (InputStreamReader streamReader = new InputStreamReader(stream);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                retBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            error(exceptionMessage);
            e.printStackTrace();
        }
        return retBuilder.toString();
    }
    
    public void saveConfig() {
        checkUsesConfig();
        try {
            config.save(getConfigFile());
        } catch (IOException ex) {
            ExceptionHandler.log(this::error, "saving config", ex);
        }
    }
    
    //
    //Abuse hashing to prevent modules with duplicate filenames.
    //
    @Override
    public int hashCode() {
        return baseFilename.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof Module && nameInterferesWith((Module) other);
    }
    
    private boolean nameInterferesWith(Module other) {
        return baseFilename.equals(other.baseFilename);
    }
    
}
