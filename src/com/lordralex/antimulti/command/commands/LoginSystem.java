/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.command.CommandManager;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author icelord871
 */
public class LoginSystem extends CommandManager implements Listener {

    List<AMPlayer> players = new ArrayList<AMPlayer>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        return "login,register";
    }

    @Override
    public String getHelp() {
        return "/login <password>, /register <password> <password>";
    }

    @EventHandler (priority=EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        int maxMoveDistance = plugin.getConfig().getInt("move-buffer", 5);
        AMPlayer amplayer = findPlayer(event.getPlayer().getName());
        if(amplayer == null)
            return;
        Location login = amplayer.getLoginLocation();
        Location newLocation = event.getTo();
        double distance = login.distanceSquared(newLocation);
        if (distance > Math.pow(maxMoveDistance, 2))
        {
            event.getPlayer().teleport(login);
            if(amplayer.registered())
                event.getPlayer().sendMessage(ChatColor.RED + "Please login");
            else
                event.getPlayer().sendMessage(ChatColor.RED + "Please register with /register <pw> <pw>");
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        AMPlayer amplayer = new AMPlayer(event.getPlayer());
        String ip = event.getAddress().getHostAddress();
        if(!requireLogin(event.getPlayer()))
        {
            amplayer.loggedIn = true;
        }
        else
        {
            players.add(amplayer);
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerJoin (PlayerJoinEvent event)
    {
        AMPlayer player = findPlayer(event.getPlayer().getName());
        if(player == null || player.loggedIn)
        {
            if(player != null) players.remove(player);
            return;
        }
        Player person = event.getPlayer();
        if(player.registered())
            person.sendMessage("Please use /login <pw>");
        else
            person.sendMessage("Please use /register <pw> <pw>");
    }

    public boolean requireLogin(Player player)
    {
        if(plugin.getConfig().getBoolean("require.login", false))
            return true;
        if(plugin.getConfig().getBoolean("require.admin-login", true) && checkPerm(player, "antimulti.admin"))
            return true;
        return false;
    }

    private AMPlayer findPlayer (String name)
    {
        for(AMPlayer player: players)
            if(player.getName().equalsIgnoreCase(name))
                return player;
        return null;
    }

    @Override
    public void reload() {
        for(int i=0; i <players.size(); i++)
        {
            AMPlayer player = players.get(i);
            if(player.loggedIn)
            {
                players.remove(i);
                i--;
            }
        }
    }

    @Override
    public void disable() {
        players.clear();
    }

    private class AMPlayer {

        private String name;
        private String password;
        private boolean loggedIn;
        private Location loginLoc;

        public AMPlayer(Player player)
        {
            name = player.getName();
            loginLoc = player.getLocation();
            File passwordFile = new File(plugin.getUserFolder(), "passwords" + File.separator + name + ".yml");
            if(!passwordFile.exists())
                passwordFile.mkdirs();
            FileConfiguration passwordConfig = YamlConfiguration.loadConfiguration(passwordFile);
            try {
            password = passwordConfig.getString("password", Encoder.encode("None"));
            } catch (NoSuchAlgorithmException e)
            {
                password = "None";
            }
        }

        public String getName()
        {
            return name;
        }

        public Location getLoginLocation()
        {
            return loginLoc;
        }

        public boolean checkPW(String test) {
            return (Encoder.areEqual(test, password));
        }

        public boolean changePW (String oldPW, String newPW)
        {
            if(!checkPW(oldPW))
                return false;
            String temp = password;
            try {
                password = Encoder.encode(newPW);
                FileConfiguration playerData = YamlConfiguration.loadConfiguration(
                        new File(plugin.getUserFolder(), "passwords" + File.separator + name + ".yml"));
                playerData.set("password", password);
                playerData.save(new File(plugin.getUserFolder(), "passwords" + File.separator + name + ".yml"));
                return true;
            } catch (IOException ex) {
                Logger.getLogger(LoginSystem.class.getName()).log(Level.SEVERE, null, ex);
                password = temp;
                return false;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LoginSystem.class.getName()).log(Level.SEVERE, null, ex);
                password = temp;
                return false;
            }
        }

        public boolean registered()
        {
            return (password.equals("None") || Encoder.areEqual(password, "None"));
        }
    }

    private static class Encoder {

        public static String encode(String message) throws NoSuchAlgorithmException
        {
            MessageDigest mdEnc = MessageDigest.getInstance("RSA");
            mdEnc.update(message.getBytes(), 0, message.length());
            String encoded = new BigInteger(1, mdEnc.digest()).toString(16);
            return encoded;
        }

        public static boolean areEqual(String message1, String message2)
        {
            try {
                if(message1.equals(message2))
                    return true;
                if(message1.equals(Encoder.encode(message2)))
                    return true;
                if(message2.equals(Encoder.encode(message1)))
                    return true;
                if(Encoder.encode(message1).equals(Encoder.encode(message2)))
                    return true;
                return false;
            } catch (NoSuchAlgorithmException e)
            {
                return false;
            }
        }
    }
}
