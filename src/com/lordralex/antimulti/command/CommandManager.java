/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.command;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author icelord871
 */
public abstract class CommandManager implements CommandExecutor {
    
    public static AntiMulti plugin;

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args);

    public abstract String getName();

    public abstract String getHelp();

    public boolean checkPerm(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (AntiMulti.perms == null) {
            if (sender.hasPermission(permission)) {
                return true;
            } else if (AntiMulti.perms.has(sender, permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPerm(Player player, String permission) {
        if (AntiMulti.perms == null) {
            if (player.hasPermission(permission)) {
                return true;
            } else if (AntiMulti.perms.has(player, permission)) {
                return true;
            }
        }
        return false;
    }

    public static void setup(AntiMulti aPlugin) {
        plugin = aPlugin;
        Whitelist whitelist = new Whitelist();
        plugin.getCommand(whitelist.getName()).setExecutor(whitelist);
        Add add = new Add();
        plugin.getCommand(add.getName()).setExecutor(add);
    }
}
