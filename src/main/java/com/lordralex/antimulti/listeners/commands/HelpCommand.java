/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.loggers.AMLogger;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class HelpCommand {
    
    public static boolean execute(CommandSender input, String[] args)
    {
        if(args.length == 0)
        {
            AMLogger.sendMessage(input, "AntiMulti Help Center", ChatColor.YELLOW);
            AMLogger.sendMessage(input, "You can also do /am <command> for help", ChatColor.BLACK);
            for(int i=1; i< CommandListener.cmds.length; i++)
            {
                AMLogger.sendMessage(input, "- " + CommandListener.cmds[i], ChatColor.YELLOW);
            }
            return true;
        }
        if(args.length >= 2)
        {
            AMLogger.sendMessage(input, "You cannot put " + args.length  + " arguments in, just one", ChatColor.RED);
            return true;
        }
        String command = args[0];
        String[] cmds = CommandListener.cmds;
        ArrayList<String> cmdList = new ArrayList<String>();
        cmdList.addAll(Arrays.asList(cmds));
        if(cmdList.contains(command))
        {
            AMLogger.sendMessage(input, "This is not done yet in this version, sorry!", ChatColor.RED);
            AMLogger.sendMessage(input, "Please visit the plugin page for command help", ChatColor.RED);
        }
        else
        {
            AMLogger.sendMessage(input, "That command was not found, use /am for all commands", ChatColor.RED);
            return true;
        }
        return true;
    }
    
}
