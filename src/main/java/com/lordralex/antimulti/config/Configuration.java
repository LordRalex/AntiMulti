/*
 * Copyright (C) 2013 Lord_Ralex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
