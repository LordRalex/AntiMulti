package com.lordralex.antimulti.files;

import com.lordralex.antimulti.config.Configuration;
import org.bukkit.entity.Player;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class DataManager implements Manager {

    private Manager manager;

    public DataManager() {
        if (Configuration.useSQL()) {
            manager = new SQLManager();
        } else {
            manager = new FlatFileManager();
        }
        manager = manager.setup();
    }

    public String[] getIPs(String name) {
        if (name == null) {
            return null;
        }
        return manager.getIPs(name.toLowerCase().trim());
    }

    public String[] getIPs(Player player) {
        if (player == null) {
            return null;
        }
        return getIPs(player.getName());
    }

    public String[] getNames(String ip) {
        if (ip == null) {
            return new String[0];
        }
        return manager.getNames(ip.toLowerCase().trim());
    }

    public String[] getNames(Player player) {
        if (player == null) {
            return new String[0];
        }
        return manager.getNames(player.getAddress().getAddress().getHostAddress());
    }

    public void addName(String ip, String name) {
        if (name == null || ip == null) {
            return;
        }
        manager.addName(name.toLowerCase().trim(), ip.toLowerCase().trim());
    }

    public void addName(Player player) {
        if (player == null) {
            return;
        }
        addName(player.getAddress().getAddress().getHostAddress(), player.getName());
    }

    public void addIP(String name, String ip) {
        if (name == null || ip == null) {
            return;
        }
        manager.addIP(name.toLowerCase().trim(), ip.toLowerCase().trim());
    }

    public void addIP(Player player) {
        if (player == null) {
            return;
        }
        addIP(player.getName(), player.getAddress().getAddress().getHostAddress());
    }

    public void reload() {
        manager.close();
        if (Configuration.useSQL()) {
            manager = new SQLManager();
        } else {
            manager = new FlatFileManager();
        }
        manager = manager.setup();
    }

    public Manager setup() {
        return manager.setup();
    }

    public void close() {
        manager.close();
    }
}
