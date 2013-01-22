package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.AMCommand;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.logger.AMLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class Whitelist implements AMCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("status")) {
            String status = "The AntiMulti Whitelist is currently "
                    + ( AntiMulti.getPlugin().getPlayerListener().getWhitelistStatus()
                    ? "on"
                    : "off" );
            sender.sendMessage(ChatColor.GREEN + status);
            return true;
        }
        String newMode = args[0];
        if (newMode.equalsIgnoreCase("on") || newMode.equalsIgnoreCase("enable")) {
            AntiMulti.getPlugin().getPlayerListener().toggleWhitelist(true);
            sender.sendMessage(ChatColor.RED + "The AntiMulti Whitelist has been engaged");
            sender.sendMessage(ChatColor.RED + "Kicking unauthorized personnel");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (!player.hasPermission("antimulti.whitelist")) {
                        player.kickPlayer(Configuration.getWhitelistMessage());
                    }
                }
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (player.hasPermission("antimulti.whitelist.notify")) {
                        player.sendMessage(ChatColor.RED + sender.getName() + " has activated the AntiMulti Whitelist");
                    }
                }
            }
            if (!( sender instanceof ConsoleCommandSender )) {
                AMLogger.info("AntiMulti whitelist has been activated by " + sender.getName());
            }
            return true;
        } else if (newMode.equalsIgnoreCase("off") || newMode.equalsIgnoreCase("disable")) {
            AntiMulti.getPlugin().getPlayerListener().toggleWhitelist(false);
            sender.sendMessage(ChatColor.GREEN + "The AntiMulti whitelist has been deactivated");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (player.hasPermission("antimulti.whitelist.notify")) {
                        player.sendMessage(ChatColor.GREEN + sender.getName() + " has deactivated the AntiMulti Whitelist");
                    }
                }
            }
            if (!( sender instanceof ConsoleCommandSender )) {
                AMLogger.info("AntiMulti whitelist has been deactivated by " + sender.getName());
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "The command usage is " + getHelp());
            return true;
        }
    }

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public String getHelp() {
        return "whitelist <on|enable|off|disable|status>";
    }
}
