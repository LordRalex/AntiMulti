/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.listener.PlayerListener;
import java.io.File;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Joshua
 */
public class AntiMulti extends JavaPlugin {

    public static final Logger logger = Bukkit.getLogger();
    public static Permission perms = null;
    public static PlayerListener pListener;
    
    @Override
    public void onLoad()
    {
        String currentVersion = this.getDescription().getVersion();
        
    }

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            String configVersion = config.getString("version");
            if (configVersion != null && !configVersion.equalsIgnoreCase(getDescription().getVersion())) {
                logger.info("[AM] An older config has been detected");
                logger.info("[AM] This is going to be annoying to convert...");
                logger.info("[AM] However, I will do my best to update your config");
                //TODO: ADD CONFIG
            }
        } else {
            saveDefaultConfig();
        }

        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        } else {
            perms = null;
        }
        if (perms == null) {
            logger.info("[AM] Vault not found, using SuperPerms, which is not really of much concern to you");
            logger.info("[AM] While I prefer you used Vault, I can work with this still");
        } else {
            logger.info("[AM] Using Vault for your permissions handler");
            logger.info("[AM] This means any permissions plugin you have will be fine, usually");
        }

        pListener = new PlayerListener(this);
        Bukkit.getPluginManager().registerEvents(pListener, this);
        CommandManager.setup(this);
    }

    @Override
    public void onDisable() {
        for(Player player: Bukkit.getOnlinePlayers())
            player.kickPlayer("Server is restarting");
        logger.info("[AM] Disabling");
        perms = null;
        Bukkit.getScheduler().cancelTasks(this);
        logger.info("[AM] Disabled");
    }

    public File getUserFolder() {
        return new File(getDataFolder(), "userData");
    }
    
    public boolean checkForUpdate()
    {
        return false;
    }
    
    private class updateThread extends Thread
    {
        String currentVersion = "2.0.0";
        
        public updateThread(String version)
        {
            
        }
    }            
}
