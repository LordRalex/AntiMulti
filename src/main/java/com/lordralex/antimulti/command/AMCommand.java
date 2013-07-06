package com.lordralex.antimulti.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public interface AMCommand extends CommandExecutor {

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args);

    public abstract String getName();

    public abstract String getHelp();
}
