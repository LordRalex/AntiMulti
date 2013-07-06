/*
 * Copyright (C) 2013 Lord_Ralex
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.AMCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class Whitelist implements AMCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        AntiMulti plugin = AntiMulti.getPlugin();
        if (args.length == 0 || args[0].equalsIgnoreCase("status")) {
            String status = "The AntiMulti Whitelist is currently "
                    + (plugin.getPlayerListener().getWhitelistStatus()
                    ? "on"
                    : "off");
            sender.sendMessage(ChatColor.GREEN + status);
            return true;
        }
        String newMode = args[0];
        if (newMode.equalsIgnoreCase("on") || newMode.equalsIgnoreCase("enable")) {
            plugin.getPlayerListener().toggleWhitelist(true);
            sender.sendMessage(ChatColor.RED + "The AntiMulti Whitelist has been engaged");
            sender.sendMessage(ChatColor.RED + "Kicking unauthorized personnel");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (!player.hasPermission("antimulti.whitelist")) {
                        player.kickPlayer(plugin.getConfiguration().getString("messages.whitelist"));
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
            if (!(sender instanceof ConsoleCommandSender)) {
                plugin.getLogger().info("AntiMulti whitelist has been activated by " + sender.getName());
            }
            return true;
        } else if (newMode.equalsIgnoreCase("off") || newMode.equalsIgnoreCase("disable")) {
            plugin.getPlayerListener().toggleWhitelist(false);
            sender.sendMessage(ChatColor.GREEN + "The AntiMulti whitelist has been deactivated");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.equals(sender)) {
                    if (player.hasPermission("antimulti.whitelist.notify")) {
                        player.sendMessage(ChatColor.GREEN + sender.getName() + " has deactivated the AntiMulti Whitelist");
                    }
                }
            }
            if (!(sender instanceof ConsoleCommandSender)) {
                AntiMulti.getPlugin().getLogger().info("AntiMulti whitelist has been deactivated by " + sender.getName());
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
