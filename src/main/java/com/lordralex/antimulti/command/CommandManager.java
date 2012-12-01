package com.lordralex.antimulti.command;

import com.lordralex.antimulti.command.commands.Add;
import com.lordralex.antimulti.command.commands.Reload;
import com.lordralex.antimulti.command.commands.Whitelist;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public class CommandManager {

    protected JavaPlugin plugin;
    protected List<AMCommand> commands = new ArrayList<AMCommand>();

    /**
     * Sets up the CommandManager. This includes creating the CommandManager
     * instances for each command, define those commands with Bukkit, and return
     * the list.
     *
     * @param aPlugin The AntiMulti instance
     * @return List of registered commands
     */
    public CommandManager(JavaPlugin aPlugin) {
        plugin = aPlugin;

        Whitelist whitelist = new Whitelist();
        plugin.getCommand(whitelist.getName()).setExecutor(whitelist);
        commands.add(whitelist);

        Add add = new Add();
        plugin.getCommand(add.getName()).setExecutor(add);
        commands.add(add);

        Reload reload = new Reload();
        plugin.getCommand(reload.getName()).setExecutor(reload);
        commands.add(reload);
    }

    /**
     * Returns a list of commands and their {@link CommandManager}s that are
     * registered with this manager.
     *
     * @return List containing all the {@link CommandManager} commands
     * registered
     */
    public List<AMCommand> getCommands() {
        return commands;
    }

    /**
     * Disables all the commands registered and removes their executors. This
     * should be used only when the server is stopped
     */
    public void stop() {
        for (AMCommand exec : commands) {
            for (String name : exec.getName().split(",")) {
                plugin.getCommand(name).setExecutor(null);
            }
        }
        commands.clear();
    }
}
