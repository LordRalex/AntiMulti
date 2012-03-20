/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.loggers.AMLogger;
import com.lordralex.antimulti.mySQL.SQL;
import com.lordralex.antimulti.mySQL.SQLDataException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Joshua
 */
public class BanAll {
    
    public static boolean execute(CommandSender input, String args[]) throws SQLDataException
    {
        if(args.length == 0)
        {
            AMLogger.sendMessage(input, "No player specified", ChatColor.RED);
            AMLogger.sendMessage(input, "- /banall <name>", ChatColor.RED);
            return true;
        }
        String[] ips = SQL.getIPs(args[0]);
        for(String ip: ips)
        {
            Bukkit.getServer().banIP(ip);
            String[] names = SQL.getNames(ip);
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