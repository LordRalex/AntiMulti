/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.loggers.AMLogger;
import com.lordralex.antimulti.files.FileManager;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class Add {

    public static boolean execute(CommandSender cs, String[] args) {
        {
            if ((AntiMulti.perms != null && AntiMulti.perms.has(cs, "antimulti.cmd.add")) || cs.hasPermission("antimulti.cmd.add")) {
                AMLogger.sendMessage(cs, CommandListener.noPermission, ChatColor.RED);
            }
        }
        if(args.length != 2)
        {
            AMLogger.sendMessage(cs, "You did not use the command right", ChatColor.RED);
            AMLogger.sendMessage(cs, "/add <name> <ip>", ChatColor.RED);
            return true;
        }
        String name = args[0];
        String ip = args[1];
        try {
            InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            AMLogger.sendMessage(cs, ip + " is not a valid IP", ChatColor.RED);
            return true;
        }
        try{
            FileManager.addIP(name, ip);
            FileManager.addName(ip, name);
            AMLogger.sendMessage(cs, "Player added successfully", ChatColor.GREEN);
            return true;
        } catch (Exception e)
        {
            AMLogger.sendMessage(cs, "Error adding player", ChatColor.RED);
            return true;
        }
    }
    
}
