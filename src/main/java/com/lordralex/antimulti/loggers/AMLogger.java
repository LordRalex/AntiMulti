/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.loggers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Joshua
 */
public class AMLogger {
    
    private static final Logger log = Bukkit.getLogger();
    ChatColor RED = ChatColor.RED;
    ChatColor GREEN = ChatColor.GREEN;
    
    public static void info(String message)
    {
        log.info(message);
    }
    
    public static void config(String message)
    {
        log.config(message);
    }
    
    public static void warning(String message)
    {
        log.warning(message);
    }
    
    public static void severe(String message)
    {
        log.severe(message);
    }
    
    public static void severe(Exception ex) {
        log.log(Level.SEVERE, null, ex);
    }
    
    public static void log(Level level, String message)
    {
        log.log(level, message);
    }
    
    public static void sendMessage(Player player, String message)
    {
        if(player == null)
            log.info(message);
        else
            player.sendMessage(message);
    }
    
    public static void sendMessage(Player player, String message, ChatColor color)
    {
        if(player == null)
            log.info(message);
        else
            player.sendMessage(color + message);
    }
    
    public static void sendMessage(CommandSender player, String message, ChatColor color)
    {
        Player input = Bukkit.getServer().getPlayer(player.getName());
        sendMessage(input, message, color);
        
    }
    
    public static Logger getLogger() {
        return log;
    }
}
