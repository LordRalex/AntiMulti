/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listener;

import com.lordralex.antimulti.AntiMulti;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

/**
 *
 * @author icelord871
 */
public class PlayerListener implements Listener {

    AntiMulti plugin;
    int connectionRate = 10;
    int maxIPs = 2;
    int maxAdminIPs = 1;
    int threadID;
    int maxNames = 1;
    int maxNamesAdmin = 1;
    boolean allowConnection = true;
    boolean whitelist;

    public PlayerListener(AntiMulti aPlugin) {
        plugin = aPlugin;
        FileConfiguration config = plugin.getConfig();
        int perSec = config.getInt("logins-per-second", 5);
        if (perSec > 20) {
            perSec = 20;
        }
        try {
            connectionRate = 20 / perSec;
        } catch (Exception e) {
            AntiMulti.logger.warning("Error with the connection rate, defaulting");
            connectionRate = 20 / 4;
        }
        maxIPs = config.getInt("max-ips", 2);
        maxAdminIPs = config.getInt("max-admin-ips", 1);
        maxNames = config.getInt("max-names", 1);
        maxNamesAdmin = config.getInt("max-admin-names", 1);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (event.getResult() != Result.ALLOWED) {
            return;
        }

        if (!throddle(event)) {
            return;
        }

        if (!whitelist(event)) {
            return;
        }

        if (!ip(event)) {
            return;
        }
    }

    private boolean throddle(PlayerLoginEvent event) {
        if (!allowConnection) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Try to connect again");
            return false;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                allowConnection = true;
            }
        }, connectionRate);
        return true;
    }

    private boolean whitelist(PlayerLoginEvent event) {
        if (!whitelist) {
            return true;
        }
        Player player = event.getPlayer();
        if (AntiMulti.perms == null) {
            if (player.hasPermission("antimulti.whitelist")) {
                return true;
            } else if (AntiMulti.perms.has(player, "antimulti.whitelist")) {
                return true;
            }
        }
        return false;
    }

    private boolean ip(PlayerLoginEvent event) {
        if(!checkNameToIp(event))
            return false;
        if(!checkIpToName(event))
            return false;
        return true;
    }

    public void toggleWhitelist(boolean newState) {
        whitelist = newState;
    }

    public boolean getWhitelistStatus() {
        return whitelist;
    }

    public boolean checkPerm(Player player, String permission) {
        if (AntiMulti.perms == null) {
            if (player.hasPermission(permission)) {
                return true;
            } else if (AntiMulti.perms.has(player, permission)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkIpToName(PlayerLoginEvent event)
    {
        String name = event.getPlayer().getName();
        String ip = event.getAddress().getHostAddress();
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File(plugin.getUserFolder(), ip + ".yml"));
        List<String> names = playerData.getStringList("names");
        if (names == null) {
            names = new ArrayList<String>();
        }
        if (names.isEmpty()) {
            names.add(name);
        } else if (names.contains(ip)) {
            return true;
        } else if (names.size() < maxNamesAdmin && checkPerm(event.getPlayer(), "antimulti.admin")) {
            names.add(name);
        } else if (names.size() < maxNames) {
            names.add(name);
        } else {
            event.disallow(Result.KICK_OTHER, "Too many names used");
            return false;
        }
        playerData.set("names", names);
        try {
            playerData.save(new File(plugin.getUserFolder(), ip + ".yml"));
            return true;
        } catch (IOException ex) {
            AntiMulti.logger.severe("ERROR SAVING PLAYER DATA FOLDER");
            AntiMulti.logger.severe("PLAYER: " + name);
            AntiMulti.logger.severe("IP: " + ip);
            event.disallow(Result.KICK_OTHER, "Error trying to handle you");
            return false;
        }
    }
    
    private boolean checkNameToIp(PlayerLoginEvent event)
    {
        String name = event.getPlayer().getName();
        String ip = event.getAddress().getHostAddress();
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File(plugin.getUserFolder(), name + ".yml"));
        List<String> ips = playerData.getStringList("ips");
        if (ips == null) {
            ips = new ArrayList<String>();
        }
        if (ips.isEmpty()) {
            ips.add(ip);
        } else if (ips.contains(ip)) {
            return true;
        } else if (ips.size() < maxAdminIPs && checkPerm(event.getPlayer(), "antimulti.admin")) {
            ips.add(ip);
        } else if (ips.size() < maxIPs) {
            ips.add(ip);
        } else {
            event.disallow(Result.KICK_OTHER, "Too many IPs used");
            return false;
        }
        playerData.set("ips", ips);
        try {
            playerData.save(new File(plugin.getUserFolder(), name + ".yml"));
            return true;
        } catch (IOException ex) {
            AntiMulti.logger.severe("ERROR SAVING PLAYER DATA FOLDER");
            AntiMulti.logger.severe("PLAYER: " + name);
            AntiMulti.logger.severe("IP" + ip);
            event.disallow(Result.KICK_OTHER, "Error trying to handle you");
            return false;
        }
    }
}