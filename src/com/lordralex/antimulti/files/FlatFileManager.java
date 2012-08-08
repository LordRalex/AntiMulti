package com.lordralex.antimulti.files;

import com.lordralex.antimulti.config.Configuration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Joshua
 */
public class FlatFileManager implements Manager {

    File ipFolder, passFolder, nameFolder;

    public FlatFileManager() {
    }

    @Override
    public Manager setup() {
        passFolder = new File(Configuration.getPlugin().getUserFolder(), "passwords");
        ipFolder = new File(Configuration.getPlugin().getUserFolder(), "ips");
        ipFolder = new File(Configuration.getPlugin().getUserFolder(), "names");
        return this;
    }

    @Override
    public String getPassword(String name) {
        File temp = new File(passFolder, name + ".yml");
        FileConfiguration user = YamlConfiguration.loadConfiguration(temp);
        String password = user.getString("password", "None");
        return password;
    }

    @Override
    public void setPassword(String name, String newPass) throws IOException {
        File temp = new File(passFolder, name + ".yml");
        FileConfiguration user = YamlConfiguration.loadConfiguration(temp);
        user.set("password", newPass);
        user.save(temp);
    }

    @Override
    public void close() {
    }

    @Override
    public String[] getIPs(String name) {
        List<String> ips = YamlConfiguration.loadConfiguration(new File(nameFolder, name + ".yml")).getStringList("ips");
        if (ips == null) {
            ips = new ArrayList<String>();
        }
        return ips.toArray(new String[0]);
    }

    @Override
    public String[] getNames(String ip) {
        List<String> names = YamlConfiguration.loadConfiguration(new File(nameFolder, ip + ".yml")).getStringList("names");
        if (names == null) {
            names = new ArrayList<String>();
        }
        return names.toArray(new String[0]);
    }

    @Override
    public void addIP(String name, String ip) {
        String[] ips = getIPs(name);
        List<String> newIPs = Arrays.asList(ips);
        if (newIPs.contains(ip)) {
            return;
        }
        YamlConfiguration.loadConfiguration(new File(ipFolder, name + ".yml")).set("ips", newIPs);
    }

    @Override
    public void addName(String ip, String name) {
        String[] names = getNames(ip);
        List<String> newNames = Arrays.asList(names);
        if (newNames.contains(name)) {
            return;
        }
        YamlConfiguration.loadConfiguration(new File(nameFolder, ip + ".yml")).set("names", newNames);
    }
}
