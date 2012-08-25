package com.lordralex.antimulti.command.commands;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.2
 */
public class Reload extends CommandManager {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        sender.sendMessage(ChatColor.RED + "Reloading AntiMulti");
        Configuration.reloadConfig();
        CommandManager.reloadAll();
        sender.sendMessage(ChatColor.GREEN + "AntiMulti reloaded");
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

    @Override
    public void reload() {
    }

    @Override
    public void disable() {
    }
}