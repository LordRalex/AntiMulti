/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.loggers.AMLogger;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class AMReload {
    
    public static boolean execute(CommandSender cs, AntiMulti plugin) throws IOException
    {
        AMLogger.sendMessage(cs, "Reloading AntiMulti config settings", ChatColor.BLUE);
        Config.loadConfig(plugin);
        AMLogger.sendMessage(cs, "Config reloaded", ChatColor.GREEN);
        return true;
    }
    
}
