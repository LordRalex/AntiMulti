package com.lordralex.antimulti.listener;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.logger.AMLogger;
import com.lordralex.antimulti.utils.IPHandler;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

/**
 * @version 3.0
 * @author Lord_Ralex
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
        } catch (IndexOutOfBoundsException e) {
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
    @EventHandler(priority = EventPriority.NORMAL)
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
        return player.hasPermission(permission);
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

    private boolean checkIpToName(PlayerLoginEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        String ip = event.getAddress().getHostAddress();
        List<String> names = Arrays.asList(plugin.getManager().getNames(ip));
        if (names.isEmpty()) {
        } else if (names.contains(name)) {
            return true;
        } else if (names.size() < maxNamesAdmin && checkPerm(event.getPlayer(), "antimulti.admin")) {
        } else if (names.size() < maxNames) {
        } else {
            event.disallow(Result.KICK_OTHER, Configuration.getMaxNameMessage());
            return false;
        }
        plugin.getManager().addName(ip, name);
        return true;
    }

    private boolean checkNameToIp(PlayerLoginEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        String ip = event.getAddress().getHostAddress();
        List<String> ips = Arrays.asList(plugin.getManager().getIPs(name));
        if (ips.isEmpty()) {
        } else if (IPHandler.contains(ips, ip)) {
            return true;
        } else if (ips.size() < maxAdminIPs && checkPerm(event.getPlayer(), "antimulti.admin")) {
        } else if (ips.size() < maxIPs) {
        } else {
            event.disallow(Result.KICK_OTHER, Configuration.getMaxIPMessage());
            return false;
        }
        plugin.getManager().addIP(name, ip);
        return true;
    }

    private boolean throddle(PlayerLoginEvent event) {
        if (!allowConnection) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Try to connect again in a second");
            return false;
        }
        if (threadID != 0) {
            Bukkit.getScheduler().cancelTask(threadID);
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
        return player.hasPermission("antimulti.whitelist");
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
}
