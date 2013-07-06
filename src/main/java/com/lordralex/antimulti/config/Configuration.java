package com.lordralex.antimulti.config;

import com.lordralex.antimulti.AntiMulti;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Configuration extends YamlConfiguration {

    private AntiMulti plugin;

    public Configuration(AntiMulti aP) {
        super();
        plugin = aP;
        if (!(new File(plugin.getDataFolder(), "config.yml").exists())) {
            plugin.getLogger().info("No config found, generating default config");
            plugin.saveDefaultConfig();
        }
        try {
            load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (FileNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "Config could not be located", ex);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred", ex);
        } catch (InvalidConfigurationException ex) {
            plugin.getLogger().log(Level.SEVERE, "The config has a yml issue", ex);
        }
        setDefaults(plugin.getConfig());
    }

    public String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }
}
