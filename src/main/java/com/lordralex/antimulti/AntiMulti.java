package com.lordralex.antimulti;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.files.DataManager;
import com.lordralex.antimulti.listener.PlayerListener;
import com.lordralex.antimulti.logger.AMLogger;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 3.0
 * @author Lord_Ralex
 * @since 1.0
 */
public class AntiMulti extends JavaPlugin {

    private PlayerListener pListener;
    private DataManager manager;
    private static AntiMulti instance;

    @Override
    public void onLoad() {
        getLogger().info("Beginning setup of " + getTitle());
        instance = this;
        AMLogger.setup(instance);
        getLogger().info("Initial setup complete");
    }

    @Override
    public void onEnable() {
        try {
            AMLogger.info(instance.getTitle() + " is now enabling");
            Configuration.loadConfig(instance);
            manager = new DataManager();
            pListener = new PlayerListener(instance);
            Bukkit.getPluginManager().registerEvents(pListener, instance);
            CommandManager.setup(instance);
            AMLogger.info(instance.getTitle() + " successfully enabled");
        } catch (Throwable ex) {
            AMLogger.error(ex, "An error occurred on startup, disabling " + instance.getTitle());
            Bukkit.getPluginManager().disablePlugin(instance);
        }
    }

    @Override
    public void onDisable() {
        AMLogger.info(getTitle() + " is now disabling");
        try {
            Bukkit.getScheduler().cancelTasks(this);
        } catch (Exception e) {
            AMLogger.error(e);
        }
        AMLogger.info(getTitle() + " is now disabled");
    }

    /**
     * Returns the folder used to store user data
     *
     * @return The File for the user folder
     */
    public File getUserFolder() {
        return new File(getDataFolder(), "data");
    }

    /**
     * Returns the version of the plugin
     *
     * @return Version
     */
    public String getVersion() {
        return instance.getDescription().getVersion();
    }

    /**
     * Returns the DataManager that is being used to read and write data
     *
     * @return The DataManager in use
     */
    public DataManager getManager() {
        return manager;
    }

    /**
     * Returns the instance of this plugin
     *
     * @return Instance of plugin
     */
    public static AntiMulti getPlugin() {
        return instance;
    }

    /**
     * Returns a title for this plugin, formatted as "[Name]-[Version]"
     *
     * @return The title of this plugin
     */
    public String getTitle() {
        return getName() + "-" + getVersion();
    }

    /**
     * Returns the PlayerListener that is being used for this plugin
     *
     * @return The PlayerListener for this plugin
     */
    public PlayerListener getPlayerListener() {
        return pListener;
    }
}
