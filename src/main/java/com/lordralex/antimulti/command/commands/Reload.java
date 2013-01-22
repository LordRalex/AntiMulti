package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.command.AMCommand;
import com.lordralex.antimulti.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class Reload implements AMCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Reloading AntiMulti configuration");
        Configuration.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "AntiMulti reloaded.");
        return true;
    }

    @Override
    public String getName() {
        return "amreload";
    }

    @Override
    public String getHelp() {
        return "/amreload";
    }
}