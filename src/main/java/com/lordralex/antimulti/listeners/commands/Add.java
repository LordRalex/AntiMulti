/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.lordralex.antimulti.loggers.AMLogger;

/**
 *
 * @author Joshua
 */
public class Add {

    public static boolean execute(CommandSender cs, String[] args) {
        if(args.length != 2)
        {
            AMLogger.sendMessage(cs, "You did not use the command right", ChatColor.RED);
            AMLogger.sendMessage(cs, "/add <name> <ip>", ChatColor.RED);
            return true;
        }
        return true;
    }
    
}
