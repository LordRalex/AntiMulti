package com.lordralex.antimulti.files;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.logger.AMLogger;
import org.bukkit.entity.Player;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class DataManager implements Manager {

    private Manager manager;

    public DataManager(AntiMulti plugin) {
        if (Configuration.useSQL()) {
            AMLogger.info("The config says to use mySQL, so starting up mySQL systems");
            manager = new SQLManager();
        } else {
            AMLogger.info("Using files to store player information");
            manager = new FlatFileManager();
        }
        manager = manager.setup(plugin);
    }

    @Override
    public String[] getIPs(String name) {
        if (name == null) {
            return new String[0];
        }
        return manager.getIPs(name.toLowerCase().trim());
    }

    public String[] getIPs(Player player) {
        return getIPs(player.getName());
    }

    @Override
    public String[] getNames(String ip) {
        if (ip == null) {
            return new String[0];
        }
        return manager.getNames(ip.toLowerCase().trim());
    }

    public String[] getNames(Player player) {
        return manager.getNames(player.getAddress().getAddress().getHostAddress());
    }

    @Override
    public void addName(String ip, String name) {
        if (name == null || ip == null) {
            return;
        }
        manager.addName(name.toLowerCase().trim(), ip.toLowerCase().trim());
    }

    public void addName(Player player) {
        addName(player.getAddress().getAddress().getHostAddress(), player.getName());
    }

    @Override
    public void addIP(String name, String ip) {
        if (name == null || ip == null) {
            return;
        }
        manager.addIP(name.toLowerCase().trim(), ip.toLowerCase().trim());
    }

    public void addIP(Player player) {
        addIP(player.getName(), player.getAddress().getAddress().getHostAddress());
    }

    @Override
    public Manager setup(AntiMulti plugin) {
        return manager.setup(plugin);
    }

    @Override
    public void close() {
        manager.close();
    }
}
