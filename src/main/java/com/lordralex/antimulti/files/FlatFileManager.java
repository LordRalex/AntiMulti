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
package com.lordralex.antimulti.files;

import com.lordralex.antimulti.AntiMulti;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class FlatFileManager implements Manager {

    private final File ipFolder, nameFolder;
    private final AntiMulti plugin;

    public FlatFileManager(AntiMulti p) {
        plugin = p;
        ipFolder = new File(plugin.getUserFolder(), "ips");
        nameFolder = new File(plugin.getUserFolder(), "names");
    }

    @Override
    public Manager setup() {
        ipFolder.mkdirs();
        nameFolder.mkdirs();
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
            plugin.getLogger().log(Level.SEVERE, "An IO error occurred", ex);
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
            plugin.getLogger().log(Level.SEVERE, "An IO error occurred", ex);
        }
    }
}
