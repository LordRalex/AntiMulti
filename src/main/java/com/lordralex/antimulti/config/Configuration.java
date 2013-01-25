package com.lordralex.antimulti.config;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.logger.AMLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class Configuration extends YamlConfiguration {

    private AntiMulti plugin;

    public Configuration(AntiMulti aP) {
        super();
        plugin = aP;
        if (!(new File(plugin.getDataFolder(), "config.yml").exists())) {
            AMLogger.info("No config found, generating default config");
            plugin.saveDefaultConfig();
        }
        try {
            load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        setDefaults(plugin.getConfig());
    }

    public String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }
}
