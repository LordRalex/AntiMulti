/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.loggers.AMLogger;
import com.lordralex.antimulti.mySQL.FileManager;
import com.lordralex.antimulti.mySQL.SQLDataException;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class GetNames {
    
    public static boolean execute(CommandSender sender, String[] args) throws SQLDataException, IOException
    {
        {
            if ((AntiMulti.perms != null && AntiMulti.perms.has(sender, "antimulti.cmd.add")) || sender.hasPermission("antimulti.cmd.add")) {
                AMLogger.sendMessage(sender, CommandListener.noPermission, ChatColor.RED);
            }
        }
        if(args.length == 0)
        {
            AMLogger.sendMessage(sender, "You did not put an ip to search", ChatColor.RED);
            return true;
        }
        String[] names = FileManager.getNames(args[0]);
        AMLogger.sendMessage(sender, "Names used by " + args[0], ChatColor.BLUE);
        for(String name: names)
            AMLogger.sendMessage(sender, "- " + name, ChatColor.BLUE);
        return true;
    } 
}
