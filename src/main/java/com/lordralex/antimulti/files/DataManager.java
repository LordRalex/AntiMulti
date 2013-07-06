package com.lordralex.antimulti.files;

import com.lordralex.antimulti.AntiMulti;
import org.bukkit.entity.Player;

public final class DataManager {

    private final Manager manager;
    private final AntiMulti plugin;

    public DataManager(AntiMulti p) {
        plugin = p;
        Manager temp;
        if (plugin.getConfiguration().getBoolean("mysql.enable", false)) {
            plugin.getLogger().info("The config says to use mySQL, so starting up mySQL systems");
            temp = new SQLManager(plugin);
        } else {
            plugin.getLogger().info("Using files to store player information");
            temp = new FlatFileManager(plugin);
        }
        manager = temp.setup();
    }

    public String[] getIPs(String name) {
        if (name == null) {
            return new String[0];
        }
        return manager.getIPs(name.toLowerCase().trim());
    }

    public String[] getIPs(Player player) {
        return getIPs(player.getName());
    }

    public String[] getNames(String ip) {
        if (ip == null) {
            return new String[0];
        }
        return manager.getNames(ip.toLowerCase().trim());
    }

    public String[] getNames(Player player) {
        return manager.getNames(player.getAddress().getAddress().getHostAddress());
    }

    public void addName(String ip, String name) {
        if (name == null || ip == null) {
            return;
        }
        manager.addName(name.toLowerCase().trim(), ip.toLowerCase().trim());
    }

    public void addName(Player player) {
        addName(player.getAddress().getAddress().getHostAddress(), player.getName());
    }

    public void addIP(String name, String ip) {
        if (name == null || ip == null) {
            return;
        }
        manager.addIP(name.toLowerCase().trim(), ip.toLowerCase().trim());
    }

    public void addIP(Player player) {
        addIP(player.getName(), player.getAddress().getAddress().getHostAddress());
    }

    public void close() {
        manager.close();
    }
}
