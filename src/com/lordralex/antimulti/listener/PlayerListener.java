package com.lordralex.antimulti.listener;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.logger.AMLogger;
import com.lordralex.antimulti.utils.IPHandler;
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
 * @version 1.2
 * @author icelord871
 * @since 1.2
 */
public class PlayerListener implements Listener {

    private AntiMulti plugin;
    private int connectionRate = 10;
    private int maxIPs = 2;
    private int maxAdminIPs = 1;
    private int threadID;
    private int maxNames = 1;
    private int maxNamesAdmin = 1;
    private boolean allowConnection = true;
    private boolean whitelist;

    /**
     * Creates the listener and sets up the local variables for use.
     *
     * @param aPlugin The AntiMulti instance
     */
    public PlayerListener(AntiMulti aPlugin) {
        plugin = aPlugin;
        int perSec = Configuration.getLoginThroddle();
        if (perSec > 20) {
            perSec = 20;
        }
        try {
            connectionRate = 20 / perSec;
            if (connectionRate < 1) {
                throw new IndexOutOfBoundsException("Connection rate too low");
            }
        } catch (Exception e) {
            AMLogger.warning("Error with the connection rate, defaulting");
            connectionRate = 20 / 4;
        }
        maxIPs = Configuration.getPlayerIPLimit();
        maxAdminIPs = Configuration.getAdminIPLimit();
        maxNames = Configuration.getPlayerNameLimit();
        maxNamesAdmin = Configuration.getAdminNameLimit();
        whitelist = Configuration.startWhitelist();
    }

    /**
     * Handles the player login event for the plugin. This is where the tests
     * are ran to determine if the player can connect or not.
     *
     * @param event The PlayerLoginEvent to associate with
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (event.getResult() != Result.ALLOWED) {
            return;
        }

        if (!throddle(event)) {
            return;
        }

        if (!checkOnline(event)) {
            return;
        }

        if (!whitelist(event)) {
            event.setResult(Result.KICK_WHITELIST);
            event.setKickMessage(Configuration.getWhitelistMessage());
            return;
        }

        if (!ip(event)) {
            return;
        }
    }

    private boolean checkOnline(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        return true;
    }

    private boolean throddle(PlayerLoginEvent event) {
        if (!allowConnection) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Try to connect again");
            return false;
        }
        threadID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

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
            return player.hasPermission("antimulti.whitelist");
        } else {
            return AntiMulti.perms.has(player, "antimulti.whitelist");
        }
    }

    private boolean ip(PlayerLoginEvent event) {
        if (!checkNameToIp(event)) {
            return false;
        }
        if (!checkIpToName(event)) {
            return false;
        }
        return true;
    }

    /**
     * Toggles the AntiMulti whitelist state.
     *
     * @param newState The new whitelist state
     */
    public void toggleWhitelist(boolean newState) {
        whitelist = newState;
    }

    /**
     * Returns the status of the whitelist.
     *
     * @return True if the whitelist is on, otherwise false
     */
    public boolean getWhitelistStatus() {
        return whitelist;
    }

    /**
     *
     * @param player The player to check
     * @param permission The permission to check
     * @return True if the player has permission, false otherwise
     */
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

    private boolean checkIpToName(PlayerLoginEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        String ip = event.getAddress().getHostAddress();
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File(plugin.getUserFolder(), ip + ".yml"));
        List<String> names = playerData.getStringList("names");
        if (names == null) {
            names = new ArrayList<String>();
        }
        for (int i = 0; i < names.size(); i++) {
            names.add(i, names.remove(i).toLowerCase());
        }
        if (names.isEmpty()) {
            names.add(name);
        } else if (names.contains(name)) {
            return true;
        } else if (names.size() < maxNamesAdmin && checkPerm(event.getPlayer(), "antimulti.admin")) {
            names.add(name);
        } else if (names.size() < maxNames) {
            names.add(name);
        } else {
            event.disallow(Result.KICK_OTHER, Configuration.getMaxNameMessage());
            return false;
        }
        playerData.set("names", names);
        try {
            playerData.save(new File(plugin.getUserFolder(), ip + ".yml"));
            return true;
        } catch (IOException ex) {
            AMLogger.severe("ERROR SAVING PLAYER DATA FOLDER");
            AMLogger.severe("PLAYER: " + name);
            AMLogger.severe("IP: " + ip);
            AMLogger.writeToFile(ex);
            event.disallow(Result.KICK_OTHER, "Error trying to handle you");
            return false;
        }
    }

    private boolean checkNameToIp(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        String ip = event.getAddress().getHostAddress();
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File(plugin.getUserFolder(), name + ".yml"));
        List<String> ips = playerData.getStringList("ips");
        if (ips == null) {
            ips = new ArrayList<String>();
        }
        if (ips.isEmpty()) {
            ips.add(ip);
        } else if (IPHandler.contains(ips, ip)) {
            return true;
        } else if (ips.size() < maxAdminIPs && checkPerm(event.getPlayer(), "antimulti.admin")) {
            ips.add(ip);
        } else if (ips.size() < maxIPs) {
            ips.add(ip);
        } else {
            event.disallow(Result.KICK_OTHER, Configuration.getMaxIPMessage());
            return false;
        }
        playerData.set("ips", ips);
        try {
            playerData.save(new File(plugin.getUserFolder(), name + ".yml"));
            return true;
        } catch (IOException ex) {
            AMLogger.severe("ERROR SAVING PLAYER DATA FOLDER");
            AMLogger.severe("PLAYER: " + name);
            AMLogger.severe("IP" + ip);
            AMLogger.writeToFile(ex);
            event.disallow(Result.KICK_OTHER, "Error trying to handle you");
            return false;
        }
    }

    /**
     * Cancels the login throddle thread. This will reset the throddle, although
     * really should not be used.
     */
    public void cancelLoginThroddle() {
        Bukkit.getScheduler().cancelTask(threadID);
    }
}
