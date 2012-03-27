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
import com.lordralex.antimulti.mySQL.Encoder;
import com.lordralex.antimulti.mySQL.SQLDataException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class Register {

    public static boolean execute(CommandSender cs, String[] args) throws SQLDataException, NoSuchAlgorithmException, IOException {
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
        if(args.length != 2)
        {
            AMLogger.sendMessage(cs, "Please enter it twice", ChatColor.RED);
            AMLogger.sendMessage(cs, "Like /am 123 123", ChatColor.RED);
            return true;
        }
        if(!args[0].equals(args[1]))
        {
            AMLogger.sendMessage(cs, "Your passwords did not match", ChatColor.RED);
            return true;
        }
        person.setPassword(args[0], args[1]);
        AMLogger.sendMessage(cs, "Your password is now " + args[0], ChatColor.GREEN);
        person.login(Encoder.encode(args[0]));
        AMLogger.sendMessage(cs, "You are now logged in", ChatColor.GREEN);
        return true;
    }
    
}
