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
