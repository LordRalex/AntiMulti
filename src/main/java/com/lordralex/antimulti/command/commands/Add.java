package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.AMCommand;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
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
            String[] testIP = args[1].split(".");
            if (testIP.length != 4) {
                throw new UnknownHostException();
            }
            for (String test : testIP) {
                try {
                    int num = Integer.parseInt(test);
                    if (num < 0 || num > 255) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    throw new UnknownHostException();
                }
            }
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
