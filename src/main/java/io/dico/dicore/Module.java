package io.dico.dicore;

import io.dico.dicore.util.Logging;
import io.dico.dicore.util.Registrator;
import io.dico.dicore.util.exceptions.Exceptions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.*;
import java.util.Objects;

public class Module<Manager extends ModuleManager> extends Logging.SubLogging {
    private final Manager manager;
    private final String name;
    private final boolean usesConfig;
    private final boolean debugging;
    private final String baseFilename;
    private FileConfiguration config;
    private boolean enabled;

    protected Module(String name, Manager manager, boolean usesConfig, boolean debugging) {
        super(name, manager, debugging);
        this.manager = Objects.requireNonNull(manager);
        this.name = name;
        this.usesConfig = usesConfig;
        this.debugging = debugging;
        baseFilename = name.toLowerCase().replace(" ", "_");
    }

    protected void enable() {

    }

    protected void disable() {

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

        this.enabled = enabled;
        if (enabled) {
            info("Enabling");
            if (this instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) this, manager.getPlugin());
            }
            Exceptions.runSafe(this::enable);
        } else {
            info("Disabling");
            Exceptions.runSafe(this::disable);
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

        InputStream configStream;
        try {
            configStream = new FileInputStream(getConfigFile());
        } catch (FileNotFoundException e) {
            configStream = null;
        }

        config = loadYaml(configStream, "config");

        final InputStream defaultConfigStream = getDefaultConfigFile();
        if (defaultConfigStream != null) {
            config.setDefaults(loadYaml(defaultConfigStream, "default config"));
            config.options().copyDefaults(true);
        }

        saveConfig();
    }

    private String prefix(Object o) {
        return String.format(" [%s]%s", name, String.valueOf(o));
    }

    @Override
    public void error(Object o) {
        manager.error(prefix(o));
    }

    @Override
    public void info(Object o) {
        manager.info(prefix(o));
    }

    @Override
    public void debug(Object o) {
        if (debugging) {
            manager.info(" [DEBUG]" + prefix(String.valueOf(o)));
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
        } catch (IOException e) {
            error("Failed to save config file for " + getName());
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

    protected void update() {

    }

}
