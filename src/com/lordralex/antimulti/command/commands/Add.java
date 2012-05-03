/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.CommandManager;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author icelord871
 */
public class Add extends CommandManager{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(args.length < 2)
        {
            sender.sendMessage(ChatColor.RED + "You did not put enough arguments");
            sender.sendMessage(ChatColor.RED + getHelp());
            return true;
        }
        try {
            InetAddress ipTest = InetAddress.getByName(args[1]);
        } catch (UnknownHostException ex) {
            sender.sendMessage(ChatColor.RED + args[1] + " is not a valid IP");
            return true;
        }
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(new File(plugin.getUserFolder(), args[0] + ".yml"));
        List<String> currentIPs = playerConfig.getStringList("ips");
        if(currentIPs == null)
            currentIPs = new ArrayList<String>();
        if(currentIPs.isEmpty())
            currentIPs.add(args[1]);
        if(!currentIPs.contains(args[1]))
            currentIPs.add(args[1]);
        playerConfig.set("ips", currentIPs);
        try {
            playerConfig.save(new File(plugin.getUserFolder(), args[0] + ".yml"));
        } catch (IOException ex) {
            sender.sendMessage(ChatColor.RED + "ERROR SAVING FILE");
            AntiMulti.logger.throwing("Add.java", "onCommand", ex);
            return true;
        }
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
