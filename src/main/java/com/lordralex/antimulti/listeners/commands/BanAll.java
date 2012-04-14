/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.files.FileManager;
import com.lordralex.antimulti.files.SQLDataException;
import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.loggers.AMLogger;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Joshua
 */
public class BanAll {
    
    public static boolean execute(CommandSender input, String args[]) throws SQLDataException, IOException
    {
        {
            if ((AntiMulti.perms != null && AntiMulti.perms.has(input, "antimulti.cmd.banall")) || input.hasPermission("antimulti.cmd.banall")) {
                AMLogger.sendMessage(input, CommandListener.noPermission, ChatColor.RED);
            }
        }
        if(args.length == 0)
        {
            AMLogger.sendMessage(input, "No player specified", ChatColor.RED);
            AMLogger.sendMessage(input, "- /banall <name>", ChatColor.RED);
            return true;
        }
        String[] ips = FileManager.getIPs(args[0]);
        for(String ip: ips)
        {
            Bukkit.getServer().banIP(ip);
            String[] names = FileManager.getNames(ip);
            for(String name: names)
            {
                Player player = Bukkit.getServer().getPlayer(name);
                player.setBanned(true);
            }
        }
        AMLogger.sendMessage(input, args[0] + "'s IPs and all accounts associated with those IPs have been banned", ChatColor.GREEN);
        return true;
    }
    
}
