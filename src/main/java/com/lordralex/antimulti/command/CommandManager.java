package com.lordralex.antimulti.command;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.commands.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class CommandManager {

    protected AntiMulti plugin;
    protected List<AMCommand> commands = new ArrayList<AMCommand>();

    /**
     * Sets up the CommandManager. This includes creating the CommandManager
     * instances for each command, define those commands with Bukkit, and return
     * the list.
     *
     * @param aPlugin The Plugin instance
     */
    public CommandManager(AntiMulti aPlugin) {
        plugin = aPlugin;

        if (plugin.getConfiguration().getBoolean("whitelist.override-vanilla", true)) {
            Whitelist whitelist = new Whitelist();
            plugin.getCommand(whitelist.getName()).setExecutor(whitelist);
            commands.add(whitelist);
        }

        Add add = new Add();
        plugin.getCommand(add.getName()).setExecutor(add);
        commands.add(add);
    }

    /**
     * Returns a list of commands and their {@link AMCommand}s that are
     * registered with this manager.
     *
     * @return List containing all the {@link AMCommand} commands registered
     */
    public List<AMCommand> getCommands() {
        return commands;
    }
}
