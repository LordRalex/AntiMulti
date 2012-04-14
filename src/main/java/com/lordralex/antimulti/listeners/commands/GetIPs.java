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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class GetIPs {
    
    public static boolean execute(CommandSender sender, String[] args) throws SQLDataException, IOException
    {
        {
            if ((AntiMulti.perms != null && AntiMulti.perms.has(sender, "antimulti.cmd.getips")) || sender.hasPermission("antimulti.cmd.getips")) {
                AMLogger.sendMessage(sender, CommandListener.noPermission, ChatColor.RED);
            }
        }
        if(args.length == 0)
        {
            AMLogger.sendMessage(sender, "You did not put a name to search", ChatColor.RED);
            return true;
        }
        String[] names = FileManager.getIPs(args[0]);
        AMLogger.sendMessage(sender, "IPs used by " + args[0], ChatColor.BLUE);
        for(String name: names)
            AMLogger.sendMessage(sender, "- " + name, ChatColor.BLUE);
        return true;
    }
}
