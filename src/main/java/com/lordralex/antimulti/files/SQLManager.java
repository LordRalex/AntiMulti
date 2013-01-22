package com.lordralex.antimulti.files;

import amlib.PatPeter.SQLibrary.MySQL;
import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.logger.AMLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class SQLManager implements Manager {

    private MySQL mysql;
    private Connection conn;

    @Override
    public Manager setup(AntiMulti plugin) {
        String[] info = Configuration.getSQLInfo();
        mysql = new MySQL(plugin.getLogger(), "[AM]", info[0], info[1], info[4], info[2], info[3]);
        conn = mysql.getConnection();
        mysql.open();
        if (mysql.checkConnection()) {
            try {
                PreparedStatement pre1 = mysql.prepare("CREATE TABLE IF NOT EXISTS amips (ip VARCHAR(16), names VARCHAR(160), PRIMARY KEY (ip));");
                pre1.executeUpdate();
                PreparedStatement pre2 = mysql.prepare("CREATE TABLE IF NOT EXISTS amnames (name VARCHAR(16), ips VARCHAR(160), PRIMARY KEY (name));");
                pre2.executeUpdate();
                return this;
            } catch (SQLException e) {
                AMLogger.error(e, "Unable to query database, changing to flat files");
            }
        }
        AMLogger.severe("Unable to connect to mySQL database, using flatfile system");
        Manager manager = new FlatFileManager();
        manager.setup(plugin);
        return manager;
    }

    @Override
    public void close() {
        try {
            conn.close();
            mysql.close();
        } catch (SQLException ex) {
            AMLogger.error(ex);
        }
    }

    @Override
    public String[] getIPs(String name) {
        try {
            PreparedStatement pre = mysql.prepare("SELECT ips FROM amnames WHERE name= ?");
            pre.setString(1, name);
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
            AMLogger.error(ex);
            return new String[0];
        }
    }

    @Override
    public String[] getNames(String ip) {
        try {
            PreparedStatement pre = mysql.prepare("SELECT names FROM amips WHERE ip= ?");
            pre.setString(1, ip);
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
            AMLogger.error(ex);
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
            PreparedStatement pre = mysql.prepare("REPLACE INTO amnames (name,ips) VALUES (? , ?)");
            pre.setString(1, name);
            pre.setString(2, newIPs);
            pre.executeUpdate();
        } catch (SQLException ex) {
            AMLogger.error(ex);
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
            PreparedStatement pre = mysql.prepare("REPLACE INTO amips (ip,names) VALUES (? , ?)");
            pre.setString(1, ip);
            pre.setString(1, newNames);
            pre.executeUpdate();
        } catch (SQLException ex) {
            AMLogger.error(ex);
        }
    }
}
