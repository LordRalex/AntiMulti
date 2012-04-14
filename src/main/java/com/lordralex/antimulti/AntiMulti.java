/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti;

import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.data.Searcher;
import com.lordralex.antimulti.files.FileManager;
import com.lordralex.antimulti.listeners.BlockListener;
import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.listeners.PlayerListener;
import com.lordralex.antimulti.loggers.AMLogger;
import com.lordralex.antimulti.tracker.Metrics;
import java.io.IOException;
import java.util.ArrayList;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Joshua
 */
public class AntiMulti extends JavaPlugin{
    
    public ArrayList<AMPlayer> playersOnServer = new ArrayList<AMPlayer>();
    PlayerListener pListener;
    CommandListener cListener;
    BlockListener bListener;
    public static Permission perms = null;
        
    @Override
    public void onEnable()
    {
        String version = this.getDescription().getVersion();
        AMLogger.info("[AntiMulti V" + version + "] Starting up");
        Bukkit.getScheduler().cancelTasks(this);
        for(Player player: Bukkit.getServer().getOnlinePlayers()) {
            player.kickPlayer("Server reloaded, please log back in");
        }
        try{
            setUpListeners();
            setUpClasses();
            FileManager.openConnection();
            setupPermissions();
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (Exception e) {
                AMLogger.warning("Could not get the Metric attachment running");
            }
            AMLogger.info("AntiMulti started up successfully");
        }
        catch (Exception e)
        {
            AMLogger.severe(e);
            this.getPluginLoader().disablePlugin(this);
            AMLogger.info("AntiMulti started up with errors");
        }
    }
    
    @Override
    public void onDisable()
    {
        AMLogger.info("AntiMulti is shutting down");
        Bukkit.getScheduler().cancelTasks(this);
        for(Player player: Bukkit.getServer().getOnlinePlayers())
            player.kickPlayer("Server is shutting down, please wait");
        pListener = null; bListener = null; cListener = null;
        FileManager.closeConnection();
        AMLogger.info("AntiMulti has shut down");
    }
    
    private void setUpClasses() throws IOException
    {
        Searcher.setup(this);
        Config.loadConfig(this);
    }
    
    private void setUpListeners()
    {
        pListener = new PlayerListener(this);
        bListener = new BlockListener();
        cListener = new CommandListener(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(bListener, this);
        pm.registerEvents(pListener, this);
        getCommand("am").setExecutor(cListener);
        getCommand("getIPs").setExecutor(cListener);
        getCommand("getNames").setExecutor(cListener);
        getCommand("add").setExecutor(cListener);
        getCommand("whitelist").setExecutor(cListener);
        getCommand("banall").setExecutor(cListener);
        getCommand("register").setExecutor(cListener);
        getCommand("login").setExecutor(cListener);
    }
    
    public void setupPermissions()
    {
        if(getServer().getPluginManager().isPluginEnabled("Vault"))
        {
            perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        }
        else {
            perms = null;
        }
    }
}
