/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.loggers.AMLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Joshua
 */
public class WhitelistCommand {
    
    public static boolean execute(CommandSender input, String[] args)
    {
        {
            if ((AntiMulti.perms != null && AntiMulti.perms.has(input, "antimulti.cmd.add")) || input.hasPermission("antimulti.cmd.add")) {
                AMLogger.sendMessage(input, CommandListener.noPermission, ChatColor.RED);
            }
        }
        if(args.length == 0 || args[0].equalsIgnoreCase("status"))
        {
            String message = "The group whitelist is currently ";
            if(Config.enableWhitelist)
                message += "on";
            else
                message += "off";
            AMLogger.sendMessage(input, message, ChatColor.GREEN);
            return true;
        }
        String arg = args[0];
        if(arg.equalsIgnoreCase("on") || arg.equalsIgnoreCase("enable"))
        {
            Config.enableWhitelist = true;
            AMLogger.sendMessage(input, "Group whitelist has been enabled", ChatColor.GREEN);
            if(input != null) 
                AMLogger.info("Group Whitelist engaged by " + input.getName());
            for(Player player: Bukkit.getServer().getOnlinePlayers())
            {
                if(!player.isOp() && !player.hasPermission("antimulti.whitelist"))
                {
                    player.kickPlayer("Whitelist active");
                }
                if(player.isOp() || player.hasPermission("antimulti.whitelist.notify"))
                {
                    String message;
                    if(input == null)
                        message = "Console";
                    else
                        message = input.getName();
                    message += " has activated the group whitelist";
                    AMLogger.sendMessage(player, message, ChatColor.RED);
                }
            }
            return true;
        } else if (arg.equalsIgnoreCase("off") || arg.equalsIgnoreCase("disable"))
        {
            Config.enableWhitelist = false;
            AMLogger.sendMessage(input, "Group whitelist has been disabled", ChatColor.GREEN);
            if(input != null) 
                AMLogger.info("Group Whitelist deactivated by " + input.getName());
            for(Player player: Bukkit.getServer().getOnlinePlayers()) {
                if(player.isOp() || player.hasPermission("antimulti.whitelist.notify"))
                {
                    String message;
                    if(input == null)
                        message = "Console";
                    else
                        message = input.getName();
                    message += " has activated the group whitelist";
                    AMLogger.sendMessage(player, message, ChatColor.RED);
                }
            }
            return true;
        }
        return true;
    }
    
}
