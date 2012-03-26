/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.listeners.commands.*;
import com.lordralex.antimulti.loggers.AMLogger;
import com.lordralex.antimulti.mySQL.SQLDataException;
import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class CommandListener implements CommandExecutor{
    
    AntiMulti plugin;
    static public String[] cmds = {
        "am",
        "getIPs",
        "getNames",
        "add",
        "whitelist",
        "banall",
        "register",
        "login"
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
                return HelpCommand.execute(cs, args);
            if(cmd.equalsIgnoreCase("getIPs"))
                return GetIPs.execute(cs, args);
            if(cmd.equalsIgnoreCase("getNames"))
                return GetNames.execute(cs, args);
            if(cmd.equalsIgnoreCase("add"))
                return Add.execute(cs, args);
            if(cmd.equalsIgnoreCase("whitelist"))
                return WhitelistCommand.execute(cs, args);
            if(cmd.equalsIgnoreCase("banall"))
                return BanAll.execute(cs, args);
            return false;
        } catch (IOException ex) {
            AMLogger.severe(ex);
            return true;
        } catch (SQLDataException ex) {
            AMLogger.severe(ex);
            return true;
        }
    }
}
