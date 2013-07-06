/*
 * Copyright (C) 2013 Lord_Ralex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lordralex.antimulti.listener;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.utils.IPHandler;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public final class PlayerListener implements Listener {

    private final AntiMulti plugin;
    private final int connectionRate;
    private final int maxIPs;
    private final int maxAdminIPs;
    private final int maxNames;
    private final int maxNamesAdmin;
    private final String whitelistMessage;
    private final String maxIPMessage;
    private final String maxNameMessage;
    private boolean whitelist;
    private volatile boolean allowConnection;
    private int threadID;

    public PlayerListener(AntiMulti aPlugin) {
        plugin = aPlugin;
        int temp = plugin.getConfiguration().getInt("throddle", 20);
        connectionRate = temp < 0 ? 0 : temp;
        if (connectionRate < 0) {
            plugin.getLogger().warning("Your config's connection rate is below 0, will set it to 0");
        }
        maxIPs = plugin.getConfiguration().getInt("");
        maxAdminIPs = plugin.getConfiguration().getInt("");
        maxNames = plugin.getConfiguration().getInt("");
        maxNamesAdmin = plugin.getConfiguration().getInt("");
        whitelist = (plugin.getConfiguration().getBoolean("") && plugin.getConfiguration().getBoolean(""));
        whitelistMessage = plugin.getConfiguration().getString("");
        maxIPMessage = plugin.getConfiguration().getString("");
        maxNameMessage = plugin.getConfiguration().getString("");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onASyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        if (!allowConnection) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Please wait to login");
            return;
        }
        if (threadID != 0) {
            Bukkit.getScheduler().cancelTask(threadID);
        }
        if (connectionRate > 0) {
            threadID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    allowConnection = true;
                }
            }, connectionRate);
            allowConnection = false;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerLoginEvent event) {
        if (event.getResult() != Result.ALLOWED) {
            return;
        }

        if (!whitelist(event)) {
            event.setResult(Result.KICK_WHITELIST);
            event.setKickMessage(whitelistMessage);
            return;
        }

        if (!ip(event)) {
            return;
        }
    }

    public void toggleWhitelist(boolean newState) {
        whitelist = newState;
    }

    public boolean getWhitelistStatus() {
        return whitelist;
    }

    private boolean checkPerm(Player player, String permission) {
        return player.hasPermission(permission);
    }

    private boolean checkIpToName(PlayerLoginEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        String ip = event.getAddress().getHostAddress();
        List<String> names = Arrays.asList(plugin.getManager().getNames(ip));
        if (names.isEmpty()) {
        } else if (names.contains(name)) {
            //the name list contained their name, accept
            return true;
        } else if (names.size() < maxNamesAdmin && checkPerm(event.getPlayer(), "antimulti.admin")) {
        } else if (names.size() < maxNames) {
        } else {
            event.disallow(Result.KICK_OTHER, maxNameMessage);
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
            event.disallow(Result.KICK_OTHER, maxIPMessage);
            return false;
        }
        plugin.getManager().addIP(name, ip);
        return true;
    }

    private boolean whitelist(PlayerLoginEvent event) {
        if (!getWhitelistStatus()) {
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
