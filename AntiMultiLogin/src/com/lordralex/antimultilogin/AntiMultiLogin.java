package com.lordralex.antimulti;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.files.DataManager;
import com.lordralex.antimulti.listener.PlayerListener;
import com.lordralex.antimulti.logger.AMLogger;
import com.lordralex.antimultilogin.listeners.LoginListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 2.0.5
 * @author Lord_Ralex
 * @since 1.0
 */
public class AntiMultiLogin extends JavaPlugin {

    DataManager manager;
    private static AntiMulti instance;
    private LoginListener lListener;

    @Override
    public void onEnable() {
        if(!Bukkit.getPluginManager().isPluginEnabled("AntiMulti"))
        {
            
        }
        instance = (AntiMulti) Bukkit.getPluginManager().getPlugin("AntiMulti");
        try {
            manager = new DataManager();
            lListener = new LoginListener();
            Bukkit.getPluginManager().registerEvents(lListener, this);
            if (Configuration.fakeOnline() && !Bukkit.getOnlineMode()) {
                AMLogger.info("USING FAKE ONLINE MODE");
            }
            AMLogger.info(this.getName() + "-" + this.getVersion() + " successfully enabled");
        } catch (Throwable ex) {
            AMLogger.error(ex, "An error occurred on startup, disabling " + this.getName());
            Bukkit.getPluginManager().disablePlugin(this);
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
        return new File(instance.getDataFolder(), "userData");
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }
}
