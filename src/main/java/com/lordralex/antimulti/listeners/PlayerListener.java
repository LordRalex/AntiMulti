/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.data.Searcher;
import com.lordralex.antimulti.loggers.AMLogger;
import com.lordralex.antimulti.mySQL.Encoder;
import com.lordralex.antimulti.mySQL.FileManager;
import com.lordralex.antimulti.mySQL.SQLDataException;
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
import org.bukkit.event.player.*;

/**
 *
 * @author Joshua
 */
public class PlayerListener implements Listener{
    
    AntiMulti plugin;

    public PlayerListener(AntiMulti aThis) {
        plugin = aThis;
    }
    
    @EventHandler (priority=EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if(player != null) {
            for(AMPlayer amplayer: plugin.playersOnServer)
            {
                if(amplayer != null)
                {
                    if(player.equals(amplayer.player))
                    {
                        if(!amplayer.loggedIn)
                            event.setQuitMessage(null);
                        amplayer = null;
                    }
                }
            }
        }
    }
    
    @EventHandler (priority=EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player user = event.getPlayer();
        String name = user.getName();
        for(Player player: Bukkit.getServer().getOnlinePlayers())
            if(player.getName().equals(name))
            {
                user.kickPlayer("Player is already in the server");
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Player is already in the server");
                return;
            }
    }
    
    @EventHandler (priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        try {
            STATUS result = offlineCache(player);
            if(result != STATUS.ALLOWED)
                return;
            result = ipTest(player);
            if(result != STATUS.ALLOWED)
                return;
            loginSystem(player);
        } catch (Exception e)
        {
            player.kickPlayer("An error occurred, please try again");
            AMLogger.severe(e);
        }
    }
    
    @EventHandler (priority=EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if(event.isCancelled())
            return;
        AMPlayer player = Searcher.findPlayer(event.getPlayer());
        if(player == null || !player.loggedIn)
        {
            event.setCancelled(true);
            event.setMessage("");
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
            //math for distance
            //Config.travelDistance;
            double distance = current.distanceSquared(player.loginSpot);
            if(distance > Math.pow(Config.travelDistance, 2))
                person.teleport(player.loginSpot);
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
    
    private enum STATUS {
        ALLOWED,
        KICKED
    }
    
    private STATUS whitelistTest(Player user)
    {
        if(Config.enableWhitelist)
        {
            if(user == null || user.isOp() || user.hasPermission("antimulti.whitelist"))
                return STATUS.ALLOWED;
            else
            {
                user.kickPlayer(Config.messageWhitelist);
                return STATUS.KICKED;
            }
        }
        return STATUS.ALLOWED;
    }
    
    private STATUS offlineCache(Player user) throws NoSuchAlgorithmException, SQLDataException
    {
        if(Config.fake_online_mode && !Bukkit.getServer().getOnlineMode())
        {
            String pw = FileManager.getPW(user);
            if(pw == null || pw.equals(Encoder.encode("None")))
            {
                user.kickPlayer("No login data in cache");
                return STATUS.KICKED;
            }
        }
        return STATUS.ALLOWED;
    }
    
    private STATUS ipTest(Player player) throws SQLDataException, NoSuchAlgorithmException, IOException
    {
        if(Config.enableIPCheck)
        {
            String ip = player.getAddress().getAddress().getHostAddress();
            String name = player.getName();
            String[] ips = FileManager.getIPs(player);
            if(!ips[0].equals(ip))
            {
                if(ips.length == 1)
                {
                    player.kickPlayer("No login data");
                    return STATUS.KICKED;
                } else
                {
                    String pw = FileManager.getPW(player);
                    if(pw == null || Encoder.areEqual(pw, "None"))
                    {
                        player.kickPlayer("No login data");
                        return STATUS.KICKED;
                    }
                }
                boolean add = true;
                for(String test: ips)
                    if(test.equals(ip))
                        add = false;
                if(add)
                {
                    if(ips.length >= Config.maxIP)
                    {
                        player.kickPlayer("IP limit reached");
                        return STATUS.KICKED;
                    }
                    else
                    {
                        FileManager.addIP(player);
                    }
                }
            }
            String[] names = FileManager.getNames(player);
            ArrayList<String> nameList = new ArrayList<String>();
            nameList.addAll(Arrays.asList(names));
            if(!nameList.contains(name))
            {
                if(nameList.size() >= Config.maxUser)
                {
                    player.kickPlayer(Config.messageIPShared);
                    return STATUS.KICKED;
                }
                else
                {
                    FileManager.addName(player);
                }
            }
        }
        return STATUS.ALLOWED;
    }
    
    private void loginSystem(Player player) throws SQLDataException, NoSuchAlgorithmException, IOException
    {
        AMPlayer person = new AMPlayer(player);
        String pw = FileManager.getPW(player);
        person.setPassword(pw, pw);
        if(!Config.enableLogin)
        {
            if(Config.enableProtectPerm && player.hasPermission("antimulti.forceRegister"))
            {
                if(Encoder.areEqual(pw, "None"))
                {
                    AMLogger.sendMessage(player, "Register your account with /register [pw] [pw]", ChatColor.BLUE);
                } else
                {
                    AMLogger.sendMessage(player, "Please login using /login [pw]", ChatColor.BLUE);
                }
            } else {
                if(!Encoder.areEqual(pw, "None"))
                {
                    AMLogger.sendMessage(player, "Please login using /login [pw]", ChatColor.BLUE);
                }
                else
                {
                    person.setLogin(true);
                }
            }
        } else
        {
            if(Encoder.areEqual(pw, "None"))
            {
                AMLogger.sendMessage(player, "Register your account with /register [pw] [pw]", ChatColor.BLUE);
            } else
            {
                AMLogger.sendMessage(player, "Please login using /login [pw]", ChatColor.BLUE);
            }
        }
    }
}