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
import com.lordralex.antimulti.patpeter.SQLibrary.MySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public final class SQLManager implements Manager {

    private MySQL mysql;
    private Connection conn;
    private String ipTable;
    private String nameTable;
    private final AntiMulti plugin;

    public SQLManager(AntiMulti p) {
        plugin = p;
    }

    @Override
    public Manager setup() {
        String[] info = new String[]{
            plugin.getConfiguration().getString("mysql.host", "localhost"),
            plugin.getConfiguration().getString("mysql.port", "3666"),
            plugin.getConfiguration().getString("mysql.db", "antimulti"),
            plugin.getConfiguration().getString("mysql.user", "user"),
            plugin.getConfiguration().getString("mysql.pass", "")
        };
        mysql = new MySQL(plugin.getLogger(), "[AM]", info[0], info[1], info[4], info[2], info[3]);
        conn = mysql.getConnection();
        mysql.open();
        if (mysql.checkConnection()) {
            try {
                ipTable = plugin.getConfiguration().getString("mysql.table.ip", "amips");
                nameTable = plugin.getConfiguration().getString("mysql.table.name", "amnames");
                PreparedStatement pre1 = mysql.prepare("CREATE TABLE IF NOT EXISTS ? (ip VARCHAR(16), names VARCHAR(160), PRIMARY KEY (ip));");
                pre1.setString(1, ipTable);
                pre1.executeUpdate();
                PreparedStatement pre2 = mysql.prepare("CREATE TABLE IF NOT EXISTS ? (name VARCHAR(16), ips VARCHAR(160), PRIMARY KEY (name));");
                pre2.setString(1, nameTable);
                pre2.executeUpdate();
                return this;
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Unable to query database, changing to flat files", e);
            }
        }
        plugin.getLogger().severe("Unable to connect to mySQL database, using flatfile system");
        Manager manager = new FlatFileManager(plugin);
        manager.setup();
        return manager;
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "An error occured on closing SQL connection", ex);
        }
        mysql.close();
    }

    @Override
    public String[] getIPs(String name) {
        try {
            PreparedStatement pre = mysql.prepare("SELECT ips FROM ? WHERE name= ?");
            pre.setString(1, nameTable);
            pre.setString(2, name);
            ResultSet result = pre.executeQuery();
            if (result == null) {
                return null;
            }
            String ip;
            if (!result.last()) {
                ip = null;
            } else {
                result.first();
                ip = result.getString("ips");
            }
            if (ip == null) {
                return new String[0];
            } else {
                String[] ips = ip.split("$");
                return ips;
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "An SQL error occurred", ex);
            return new String[0];
        }
    }

    @Override
    public String[] getNames(String ip) {
        try {
            PreparedStatement pre = mysql.prepare("SELECT names FROM ? WHERE ip= ?");
            pre.setString(1, ipTable);
            pre.setString(2, ip);
            ResultSet result = pre.executeQuery();
            if (result == null) {
                return null;
            }
            String name;
            if (!result.last()) {
                name = null;
            } else {
                result.first();
                name = result.getString("names");
            }
            if (name == null) {
                return new String[0];
            } else {
                String[] names = name.split("$");
                return names;
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "An SQL error occurred", ex);
            return new String[0];
        }
    }

    @Override
    public void addIP(String name, String ip) {
        String[] oldIPs = getIPs(name);
        ArrayList<String> ips = new ArrayList<String>();
        ips.addAll(Arrays.asList(oldIPs));
        for (int i = 0; i < ips.size(); i++) {
            ips.add(i, ips.remove(i).toLowerCase());
        }
        if (ips.contains(ip.toLowerCase())) {
            return;
        }
        ips.add(ip.toLowerCase());
        String newIPs = "";
        for (String tempIp : ips) {
            newIPs += tempIp + " ";
        }
        newIPs = newIPs.trim().replace(" ", "$");
        try {
            PreparedStatement pre = mysql.prepare("REPLACE INTO ? (name,ips) VALUES (? , ?)");
            pre.setString(1, nameTable);
            pre.setString(2, name);
            pre.setString(3, newIPs);
            pre.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "An SQL error occurred", ex);
        }
    }

    @Override
    public void addName(String ip, String name) {
        String[] oldNames = getNames(ip);
        ArrayList<String> names = new ArrayList<String>();
        names.addAll(Arrays.asList(oldNames));
        for (int i = 0; i < names.size(); i++) {
            names.add(i, names.remove(i).toLowerCase());
        }
        if (names.contains(name.toLowerCase())) {
            return;
        }
        names.add(name.toLowerCase());
        String newNames = "";
        for (String tempName : names) {
            newNames += tempName + " ";
        }
        newNames = newNames.trim().replace(" ", "$");
        try {
            PreparedStatement pre = mysql.prepare("REPLACE INTO ? (ip,names) VALUES (? , ?)");
            pre.setString(1, ipTable);
            pre.setString(2, ip);
            pre.setString(3, newNames);
            pre.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "An SQL error occurred", ex);
        }
    }
}
