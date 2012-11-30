package com.lordralex.antimulti.files;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.logger.AMLogger;
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
        passFolder = new File(AntiMulti.getPlugin().getUserFolder(), "passwords");
        ipFolder = new File(AntiMulti.getPlugin().getUserFolder(), "ips");
        nameFolder = new File(AntiMulti.getPlugin().getUserFolder(), "names");
        return this;
    }

    @Override
    public void close() {
    }

    @Override
    public String[] getIPs(String aName) {
        String name = aName.toLowerCase().trim();
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
    public void addIP(String aName, String ip) {

        String name = aName.toLowerCase().trim();
        String[] ips = getIPs(name);
        List<String> newIPs = Arrays.asList(ips);
        if (newIPs.contains(ip)) {
            return;
        }
        FileConfiguration temp = YamlConfiguration.loadConfiguration(new File(ipFolder, name + ".yml"));
        temp.set("ips", newIPs);
        try {
            temp.save(new File(ipFolder, name + ".yml"));
        } catch (IOException ex) {
            AMLogger.error(ex);
        }
    }

    @Override
    public void addName(String ip, String name) {
        String[] names = getNames(ip);
        List<String> newNames = Arrays.asList(names);
        if (newNames.contains(name)) {
            return;
        }
        FileConfiguration temp = YamlConfiguration.loadConfiguration(new File(nameFolder, ip + ".yml"));
        temp.set("names", newNames);
        try {
            temp.save(new File(nameFolder, ip + ".yml"));
        } catch (IOException ex) {
            AMLogger.error(ex);
        }
    }
}
