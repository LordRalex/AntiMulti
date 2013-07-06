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
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class Add implements AMCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "You did not put enough arguments:");
            sender.sendMessage(ChatColor.RED + getHelp());
            return true;
        }
        try {
            InetAddress.getByName(args[1]);
        } catch (UnknownHostException ex) {
            sender.sendMessage(ChatColor.RED + args[1] + " is not a valid IP");
            return true;
        }
        AntiMulti.getPlugin().getManager().addName(args[1], args[0]);
        AntiMulti.getPlugin().getManager().addIP(args[0], args[1]);
        sender.sendMessage(ChatColor.GREEN + args[1] + " was added to " + args[0]);
        return true;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getHelp() {
        return "add <name> <ip>";
    }
}
