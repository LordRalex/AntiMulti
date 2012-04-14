/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.data.Searcher;
import com.lordralex.antimulti.files.Encoder;
import com.lordralex.antimulti.files.FileManager;
import com.lordralex.antimulti.files.SQLDataException;
import com.lordralex.antimulti.loggers.AMLogger;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.*;

/**
 *
 * @author Joshua
 */
public class PlayerListener implements Listener {

    AntiMulti plugin;
    boolean bufferTriggered = false;
    int tid;
    String[] reasons = {
        "Login buffer, please try again",
        "Player is already in the server",
        "No login data in cache",
        "No login data",
        Config.messageIPShared
    };

    public PlayerListener(AntiMulti aThis) {
        plugin = aThis;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            for(int i=0; i < plugin.playersOnServer.size(); i++)
            {
                AMPlayer amplayer = plugin.playersOnServer.get(i);
                if(amplayer != null)
                {
                    if (player.getName().equals(amplayer.player.getName())) {
                        if (!amplayer.loggedIn) {
                            event.setQuitMessage(null);
                        }
                        plugin.playersOnServer.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        try {
            final Player th = event.getPlayer();
            if (bufferTriggered) {
                event.getPlayer().kickPlayer("Login buffer, please try again");
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Login buffer, please try again");
            }
            tid = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    bufferTriggered = false;
                }
            }, Config.bufferDelay);
            bufferTriggered = true;
            Player user = event.getPlayer();
            String name = user.getName();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getName().equals(name)) {
                    user.kickPlayer("Player is already in the server");
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Player is already in the server");
                    return;
                }
            }
            Results result = whitelistTest(user);
            if (!result.allow) {
                user.kickPlayer(result.message);
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, result.message);
            }
            result = offlineCache(user);
            if (!result.allow) {
                user.kickPlayer(result.message);
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, result.message);
            }
        } catch (Exception e) {
            event.getPlayer().kickPlayer("An error occurred, please try again");
            AMLogger.severe(e);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerKick(PlayerKickEvent event) {
        String reason = event.getReason();
        for (String reasonMsg : reasons) {
            if (reason.equals(reasonMsg)) {
                event.setLeaveMessage(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            if (!Bukkit.getServer().getOnlineMode()) {
                event.setJoinMessage("");
                Results result = ipTest(player);
                if (!result.allow) {
                    event.getPlayer().kickPlayer(result.message);
                    event.setJoinMessage(null);
                    return;
                }
            }

            loginSystem(player);
            AMPlayer person = Searcher.findPlayer(player);
            if (person.loggedIn) {
                event.setJoinMessage(ChatColor.YELLOW + player.getName() + " has joined");
            }
        } catch (Exception e) {
            player.kickPlayer("An error occurred, please try again");
            AMLogger.severe(e);
            event.setJoinMessage("");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        String name = event.getName();
        String ip = event.getName();
        try {
            Results result = ipTest(name, ip);
            if (!result.allow) {
                event.disallow(Result.KICK_OTHER, result.message);
            }
        } catch (Exception e) {
            event.setKickMessage("An error occurred, please try again");
            AMLogger.severe(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        String message = event.getMessage();
        if (!player.loggedIn) {
            if (!(message.startsWith("/register") || message.startsWith("/login"))) {
                AMLogger.info("Message cancelled");
                event.setCancelled(true);
                event.setMessage("");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(PlayerChatEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerFish(PlayerFishEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player person = event.getPlayer();
        AMPlayer player = Searcher.findPlayer(person);
        if (!player.loggedIn) {
            Location current = person.getLocation();
            double distance = current.distanceSquared(player.loginSpot);
            if (distance > Math.pow(Config.travelDistance, 2)) {
                person.teleport(player.loginSpot);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if (!player.loggedIn) {
            event.setCancelled(true);
        }
    }

    private Results whitelistTest(Player user) {
        if (Config.enableWhitelist) {
            if (user == null || user.isOp() || user.hasPermission("antimulti.whitelist")) {
                return new Results();
            } else {
                user.kickPlayer(Config.messageWhitelist);
                return new Results(Config.messageWhitelist);
            }
        }
        return new Results();
    }

    private Results offlineCache(Player user) throws NoSuchAlgorithmException, SQLDataException, IOException {
        if (Config.fake_online_mode && !Bukkit.getServer().getOnlineMode()) {
            String pw = FileManager.getPW(user);
            if (pw == null || pw.equals(Encoder.encode("None"))) {
                user.kickPlayer("No login data in cache");
                return new Results("No login data in cache");
            }
        }
        return new Results();
    }

    private Results ipTest(Player player) throws SQLDataException, NoSuchAlgorithmException, IOException {
        return ipTest(player.getAddress().getAddress().getHostAddress(), player.getName());
    }

    private Results ipTest(String name, String ip) throws SQLDataException, NoSuchAlgorithmException, IOException {
        if (Config.enableIPCheck) {
            String[] ips = FileManager.getIPs(name, ip);
            if (!ips[0].equals(ip)) {
                if (ips.length == 1) {

                    return new Results("No login data");
                } else {
                    String pw = FileManager.getPW(name);
                    if (pw == null || Encoder.areEqual(pw, "None")) {
                        return new Results("No login data");
                    }
                }
                boolean add = true;
                for (String test : ips) {
                    if (test.equals(ip)) {
                        add = false;
                    }
                }
                if (add) {
                    if (ips.length >= Config.maxIP) {
                        return new Results(Config.messageIPShared);
                    } else {
                        FileManager.addIP(name, ip);
                    }
                }
            }
            String[] names = FileManager.getNames(name, ip);
            ArrayList<String> nameList = new ArrayList<String>();
            nameList.addAll(Arrays.asList(names));
            if (!nameList.contains(name)) {
                if (nameList.size() >= Config.maxUser) {
                    return new Results(Config.messageIPShared);
                } else {
                    FileManager.addName(ip, name);
                }
            }
        }
        return new Results();
    }

    private void loginSystem(Player player) throws SQLDataException, NoSuchAlgorithmException, IOException {
        AMPlayer person = new AMPlayer(player);
        String pw = FileManager.getPW(player);
        person.setPassword(pw, true);
        person.setLogin(false);
        if (!Config.enableLogin) {
            if (Config.enableProtectPerm && player.hasPermission("antimulti.forceRegister")) {
                if (Encoder.areEqual(pw, Encoder.encode("None"))) {
                    AMLogger.sendMessage(player, "Register your account with /register [pw] [pw]", ChatColor.BLUE);
                } 
                else {
                    AMLogger.sendMessage(player, "Please login using /login [pw]", ChatColor.BLUE);
                }
            } 
            else {
                if (Encoder.areEqual(pw, Encoder.encode("None"))) {
                    person.setLogin(true);
                } 
                else {
                    AMLogger.sendMessage(player, "Please login using /login [pw]", ChatColor.BLUE);
                }
            }
        } 
        else {
            if (Encoder.areEqual(pw, Encoder.encode("None"))) {
                AMLogger.sendMessage(player, "Register your account with /register [pw] [pw]", ChatColor.BLUE);
            } 
            else {
                AMLogger.sendMessage(player, "Please login using /login [pw]", ChatColor.BLUE);
            }
        }
        plugin.playersOnServer.add(person);
    }

    private class Results {

        public boolean allow;
        public String message;

        public Results() {
            allow = true;
        }

        public Results(String msg) {
            message = msg;
            allow = false;
        }
    }
}
