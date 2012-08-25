package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @version 1.0
 * @author icelord871
 * @since 1.0
 */
public class Add extends CommandManager {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "You did not put enough arguments");
            sender.sendMessage(ChatColor.RED + getHelp());
            return true;
        }
        try {
            InetAddress.getByName(args[1]);
        } catch (UnknownHostException ex) {
            sender.sendMessage(ChatColor.RED + args[1] + " is not a valid IP");
            return true;
        }
        Configuration.getPlugin().getManager().addName(args[1], args[0]);
        Configuration.getPlugin().getManager().addIP(args[0], args[1]);
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

    @Override
    public void reload() {
    }

    @Override
    public void disable() {
    }
}
