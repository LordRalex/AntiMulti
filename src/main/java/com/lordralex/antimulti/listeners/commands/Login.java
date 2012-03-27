/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.data.Searcher;
import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.loggers.AMLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class Login {

    public static boolean execute(CommandSender cs, String[] args) {
        {
            if ((AntiMulti.perms != null && AntiMulti.perms.has(cs, "antimulti.cmd.add")) || cs.hasPermission("antimulti.cmd.add")) {
                AMLogger.sendMessage(cs, CommandListener.noPermission, ChatColor.RED);
            }
        }
        String player = cs.getName();
        if(player == null || player.equalsIgnoreCase(""))
        {
            AMLogger.sendMessage(cs, "You are not a player", ChatColor.RED);
            return true;
        }
        AMPlayer person = Searcher.findPlayer(player);
        if(person == null)
        {
            AMLogger.sendMessage(cs, "Your data was not found", ChatColor.RED);
            return true;
        }
        if(args.length != 1)
        {
            AMLogger.sendMessage(cs, "Please enter it only once", ChatColor.RED);
            AMLogger.sendMessage(cs, "Like /login 123", ChatColor.RED);
            return true;
        }
        return true;
    }
    
}
