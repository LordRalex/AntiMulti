/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.listeners.commands.Add;
import com.lordralex.antimulti.listeners.commands.GetIPs;
import com.lordralex.antimulti.listeners.commands.GetNames;
import com.lordralex.antimulti.listeners.commands.HelpCommand;
import com.lordralex.antimulti.loggers.AMLogger;
import com.lordralex.antimulti.mySQL.SQLDataException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class CommandListener implements CommandExecutor{
    
    AntiMulti plugin;
    public String[] cmds = {
        "am",
        "getIPs",
        "getNames",
        "getIP",
        "add",
        "whitelist",
        "banall"
    };
    
    public CommandListener(AntiMulti aPlugin)
    {
        plugin = aPlugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        try {
            String cmd = cmnd.getName();
            
            if(cmd.equalsIgnoreCase("am"))
                return HelpCommand.execute();
            if(cmd.equalsIgnoreCase("getIPs"))
                return true;
            if(cmd.equalsIgnoreCase("getNames"))
                return GetNames.execute(cs, args);
            if(cmd.equalsIgnoreCase("getIP"))
                return GetIPs.execute(cs, args);
            if(cmd.equalsIgnoreCase("add"))
                return Add.execute(cs, args);
            if(cmd.equalsIgnoreCase("whitelist"))
                return true;
            if(cmd.equalsIgnoreCase("banall"))
                return true;
            return false;
        } catch (SQLDataException ex) {
            AMLogger.severe(ex);
            return true;
        }
    }
}
