/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public abstract class AMCommand implements CommandExecutor{
    
    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args);

    /**
     * Returns the name of this sub-command
     *
     * @return Name of command
     */
    public abstract String getName();

    /**
     * Returns the sub-command's help line
     *
     * @return Help for this command
     */
    public abstract String getHelp();
    
}