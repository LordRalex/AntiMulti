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

    public File getUserFolder() {
        return new File(getDataFolder(), "data");
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public DataManager getManager() {
        return manager;
    }

    public static AntiMulti getPlugin() {
        return instance;
    }

    public String getTitle() {
        return getName() + "-" + getVersion();
    }
    
    public PlayerListener getPlayerListener()
    {
        return pListener;
    }
}
