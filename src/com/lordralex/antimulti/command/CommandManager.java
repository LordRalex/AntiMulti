package com.lordralex.antimulti.command;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.command.commands.*;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author icelord871
 */
public abstract class CommandManager implements CommandExecutor {

    public static AntiMulti plugin;
    public static List<CommandManager> commands = new ArrayList<CommandManager>();

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args);

    public abstract String getName();

    public abstract String getHelp();

    public abstract void reload();

    public abstract void disable();

    public boolean checkPerm(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (AntiMulti.perms == null) {
            if (sender.hasPermission(permission)) {
                return true;
            } else if (AntiMulti.perms.has(sender, permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPerm(Player player, String permission) {
        if(AntiMulti.perms == null)
            return player.hasPermission(permission);
        else
            return AntiMulti.perms.has(player, permission);
    }

    public static List<CommandManager> setup(AntiMulti aPlugin) {
        plugin = aPlugin;

        Whitelist whitelist = new Whitelist();
        plugin.getCommand(whitelist.getName()).setExecutor(whitelist);
        commands.add(whitelist);

        Add add = new Add();
        plugin.getCommand(add.getName()).setExecutor(add);
        commands.add(add);

        LoginSystem login = new LoginSystem();
        String[] loginCommands = login.getName().split(",");
        if (loginCommands.length == 2) {
            plugin.getCommand(loginCommands[0]).setExecutor(login);
            plugin.getCommand(loginCommands[1]).setExecutor(login);
            Bukkit.getPluginManager().registerEvents(login, plugin);
            commands.add(login);
        } else {
            AntiMulti.logger.warning("[AM] There appears to be an issue with the login commands, will not set them up");
        }
        return getCommands();
    }

    public static List<CommandManager> getCommands()
    {
        return commands;
    }

    public static void stop()
    {
        for(CommandManager exec: commands)
        {
            exec.disable();
        }
    }

    public static void reloadAll()
    {
        for(CommandManager exec: commands)
        {
            exec.reload();
        }
    }
}
