package com.lordralex.antimulti.command;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.commands.Add;
import com.lordralex.antimulti.command.commands.Reload;
import com.lordralex.antimulti.command.commands.Whitelist;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public abstract class CommandManager implements CommandExecutor {

    protected static AntiMulti plugin;
    protected static List<CommandManager> commands = new ArrayList<CommandManager>();

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

    /**
     * Checks to see if a CommandSender has a permission. If the sender is a
     * ConsoleCommandSender, then this returns true.
     *
     * @param sender Sender to check permission
     * @param permission Permission to check
     * @return True if player has the permission, false otherwise
     */
    public static boolean checkPerm(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        return sender.hasPermission(permission);
    }

    /**
     * Checks to see if a player has a permission..
     *
     * @param player Player to check permission
     * @param permission Permission to check
     * @return True if player has the permission, false otherwise
     */
    public static boolean checkPerm(Player player, String permission) {
        return player.hasPermission(permission);
    }

    /**
     * Sets up the CommandManager. This includes creating the CommandManager
     * instances for each command, define those commands with Bukkit, and return
     * the list.
     *
     * @param aPlugin The AntiMulti instance
     * @return List of registered commands
     */
    public static List<CommandManager> setup(AntiMulti aPlugin) {
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

        return getCommands();
    }

    /**
     * Returns a list of commands and their {@link CommandManager}s that are
     * registered with this manager.
     *
     * @return List containing all the {@link CommandManager} commands
     * registered
     */
    public static List<CommandManager> getCommands() {
        return commands;
    }

    /**
     * Disables all the commands registered and removes their executors. This
     * should be used only when the server is stopped
     */
    public static void stop() {
        for (CommandManager exec : commands) {
            for (String name : exec.getName().split(",")) {
                plugin.getCommand(name).setExecutor(null);
            }
        }
        commands.clear();
    }
}
