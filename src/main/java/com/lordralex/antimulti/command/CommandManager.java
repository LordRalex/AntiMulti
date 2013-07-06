package com.lordralex.antimulti.command;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.commands.*;
import java.util.ArrayList;
import java.util.List;

public final class CommandManager {

    private AntiMulti plugin;
    private List<AMCommand> commands = new ArrayList<AMCommand>();

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

    public List<AMCommand> getCommands() {
        return commands;
    }
}
