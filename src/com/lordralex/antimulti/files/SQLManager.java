package com.lordralex.antimulti.files;

import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.logger.AMLogger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.MySQL;

/**
 * @version 1.0
 * @author Joshua
 */
public class SQLManager implements Manager {

    MySQL mysql;
    Connection conn;

    @Override
    public void setPassword(String name, String newPass) throws IOException {
        mysql.query("REPLACE INTO ampasswords (name,pass) VALUES ('" + name + "','" + newPass + "');");
    }

    @Override
    public Manager setup() {
        String[] info = Configuration.getSQLInfo();
        mysql = new MySQL(Configuration.getPlugin().getLogger(), "[AM]", info[0], info[1], info[4], info[2], info[3]);
        conn = mysql.getConnection();
        mysql.open();
        if (mysql.checkConnection()) {
            mysql.query("CREATE TABLE IF NOT EXISTS ampasswords (name VARCHAR(16), pass VARCHAR(512), PRIMARY KEY (name));");
            mysql.query("CREATE TABLE IF NOT EXISTS amips (ip VARCHAR(16), names VARCHAR(160), PRIMARY KEY (ip));");
            mysql.query("CREATE TABLE IF NOT EXISTS amnames (name VARCHAR(16), ips VARCHAR(160), PRIMARY KEY (name));");
            return this;
        } else {
            AMLogger.severe("Unable to connect to mySQL database, using flatfile system");
            Manager manager = new FlatFileManager();
            manager.setup();
            return manager;
        }
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
        ResultSet result = mysql.query("SELECT ips FROM amnames WHERE name=' " + name + "';");
        if (result == null) {
            return null;
        }
        try {
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
                String[] ips = ip.split(",");
                return ips;
            }
        } catch (SQLException ex) {
            AMLogger.error(ex);
            return new String[0];
        }
    }

    @Override
    public String[] getNames(String ip) {
        ResultSet result = mysql.query("SELECT names FROM amips WHERE ip=' " + ip + "';");
        if (result == null) {
            return null;
        }
        try {
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
                String[] names = name.split(",");
                return names;
            }
        } catch (SQLException ex) {
            AMLogger.error(ex);
            return new String[0];
        }
    }

    @Override
    public String getPassword(String name) {
        ResultSet result = mysql.query("SELECT pass FROM ampasswords WHERE name=' " + name + "';");
        if (result == null) {
            return null;
        }
        try {
            String pw;
            if (!result.last()) {
                pw = "None";
            } else {
                result.first();
                pw = result.getString("pass");
            }
            return pw;
        } catch (SQLException ex) {
            Logger.getLogger(SQLManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
        if (ips.contains(ip)) {
            return;
        }
        ips.add(ip.toLowerCase());
        String newIPs = "";
        for (String tempIp : ips) {
            newIPs += tempIp + " ";
        }
        newIPs = newIPs.trim().replace(" ", ",");
        mysql.query("REPLACE INTO amnames (name,ips) VALUES ('" + name + "','" + newIPs + "');");
    }

    @Override
    public void addName(String ip, String name) {
        String[] oldNames = getNames(ip);
        ArrayList<String> names = new ArrayList<String>();
        names.addAll(Arrays.asList(oldNames));
        for (int i = 0; i < names.size(); i++) {
            names.add(i, names.remove(i).toLowerCase());
        }
        if (names.contains(name)) {
            return;
        }
        names.add(name.toLowerCase());
        String newNames = "";
        for (String tempName : names) {
            newNames += tempName + " ";
        }
        newNames = newNames.trim().replace(" ", ",");
        mysql.query("REPLACE INTO amips (ip,names) VALUES ('" + ip + "','" + newNames + "');");
    }
}
