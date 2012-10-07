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
 * @version 2.0.5
 * @author Lord_Ralex
 * @since 1.0
 */
public class AntiMulti extends JavaPlugin {

    public static PlayerListener pListener;
    DataManager manager;
    private static AntiMulti instance;

    @Override
    public void onLoad() {
        instance = this;
        AMLogger.setup(instance);
    }

    @Override
    public void onEnable() {
        try {
            Configuration.loadConfig(instance);
            manager = new DataManager();
            pListener = new PlayerListener(instance);
            Bukkit.getPluginManager().registerEvents(pListener, instance);
            CommandManager.setup(instance);
            AMLogger.info(instance.getName() + "-" + instance.getVersion() + " successfully enabled");
        } catch (Throwable ex) {
            AMLogger.error(ex, "An error occurred on startup, disabling " + instance.getName());
            Bukkit.getPluginManager().disablePlugin(instance);
        }
    }

    @Override
    public void onDisable() {
        try {
            Bukkit.getScheduler().cancelTasks(this);
        } catch (Exception e) {
            AMLogger.error(e);
        }
        AMLogger.info("Disabled " + getVersion());
    }

    public File getUserFolder() {
        return new File(getDataFolder(), "userData");
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
}
