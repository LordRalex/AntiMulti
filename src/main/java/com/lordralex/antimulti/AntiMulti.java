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
