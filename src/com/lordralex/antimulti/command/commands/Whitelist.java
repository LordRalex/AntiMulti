/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author icelord871
 */
public class Whitelist extends CommandManager {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("status")) {
            String status = "The AntiMulti Whitelist is currently ";
            if (AntiMulti.pListener.getWhitelistStatus()) {
                status += "on";
            } else {
                status += "off";
            }
            sender.sendMessage(ChatColor.GREEN + status);
            return true;
        }
        String newMode = args[0];
        if (newMode.equalsIgnoreCase("1") || newMode.equalsIgnoreCase("on") || newMode.equalsIgnoreCase("enable")) {
            AntiMulti.pListener.toggleWhitelist(true);
            sender.sendMessage(ChatColor.RED + "The AntiMulti Whitelist has been engaged");
            sender.sendMessage(ChatColor.RED + "Kicking unauthorized personnel");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (!checkPerm(player, "antimulti.whitelist")) {
                        player.kickPlayer("Whitelist activated");
                    }
                }
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (checkPerm(player, "antimulti.whitelist.notify")) {
                        player.sendMessage(ChatColor.RED + sender.getName() + " has activated the AM Whitelist");
                    }
                }
            }
            if (!(sender instanceof ConsoleCommandSender)) {
                AntiMulti.logger.info("AntiMulti whitelist has been activated");
            }
            return true;
        }
        if (newMode.equalsIgnoreCase("0") || newMode.equalsIgnoreCase("off") || newMode.equalsIgnoreCase("disable")) {
            AntiMulti.pListener.toggleWhitelist(false);
            sender.sendMessage(ChatColor.GREEN + "The AntiMulti whitelist has been deactivated");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (checkPerm(player, "antimulti.whitelist.notify")) {
                        player.sendMessage(ChatColor.GREEN + sender.getName() + " has deactivated the AM Whitelist");
                    }
                }
            }
            if (!(sender instanceof ConsoleCommandSender)) {
                AntiMulti.logger.info("AntiMulti whitelist has been deactivated");
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Hmm, there seems to have been an issue with the command");
        return true;
    }

    @Override
    public String getName() {
        return "whitelist";
    }

    @Override
    public String getHelp() {
        return "whitelist <on|1|enable off|0|disable status>";
    }
}
