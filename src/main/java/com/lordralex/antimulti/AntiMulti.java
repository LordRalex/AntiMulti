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
package com.lordralex.antimulti;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.files.DataManager;
import com.lordralex.antimulti.listener.PlayerListener;
import java.io.File;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiMulti extends JavaPlugin {

    private PlayerListener pListener;
    private DataManager manager;
    private CommandManager cmdManager;
    private Configuration configuration;

    @Override
    public void onEnable() {
        try {
            getLogger().info("Loading configuration");
            configuration = new Configuration(this);
            getLogger().info("Setting up data managers");
            manager = new DataManager(this);
            getLogger().info("Creating player listeners");
            pListener = new PlayerListener(this);
            Bukkit.getPluginManager().registerEvents(pListener, this);
            getLogger().info("Registering commands");
            cmdManager = new CommandManager(this);
            getLogger().info("All systems operational");
        } catch (Throwable ex) {
            getLogger().log(Level.SEVERE, "An error occurred on startup, disabling " + this.getTitle(), ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public File getUserFolder() {
        return new File(getDataFolder(), "data");
    }

    public String getVersion() {
        return getDescription().getVersion();
    }

    public String getTitle() {
        return getName() + "-" + getVersion();
    }

    public DataManager getManager() {
        return manager;
    }

    public PlayerListener getPlayerListener() {
        return pListener;
    }

    public CommandManager getCommandManager() {
        return cmdManager;
    }

    public static AntiMulti getPlugin() {
        return (AntiMulti) Bukkit.getPluginManager().getPlugin("AntiMulti");
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
