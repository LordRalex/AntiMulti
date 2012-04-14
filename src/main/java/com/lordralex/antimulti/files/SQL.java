/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.files;

import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.loggers.AMLogger;
import java.sql.ResultSet;
import java.sql.SQLException;
import lib.PatPeter.SQLibrary.MySQL;

/**
 *
 * @author Joshua
 */
public class SQL {
    
    private static MySQL mysql;
    
    public static boolean testConnection()
    {
        try{
            MySQL test = new MySQL(AMLogger.getLogger(), "AntiMulti", Config.host, String.valueOf(Config.port), Config.database, Config.user, Config.pass);
            boolean good = test.open().isValid(1000);
            test.close();
            return good;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }        
    }
    
    public static void connect()
    {
        mysql = new MySQL(AMLogger.getLogger(), "AntiMulti", Config.host, String.valueOf(Config.port), Config.database, Config.user, Config.pass);
        mysql.open();
        doesDBExist();
    }
    
    public static void disconnect()
    {
        mysql.close();
    }
    
    private static void doesDBExist()
    {
        if(!mysql.checkTable("ipdb"))
            mysql.query("CREATE TABLE ipdb(ip VARCHAR(15), playernames VARCHAR(500));");
        if(!mysql.checkTable("logindb"))
            mysql.query("CREATE TABLE logindb(playername VARCHAR(16), password VARCHAR(500));");
        if(!mysql.checkTable("userdb"))
            mysql.query("CREATE TABLE userdb(playername VARCHAR(16), ips VARCHAR(500));");
    }
    
    public static String getPW(String name) throws SQLDataException
    {
        ResultSet rs = mysql.query("SELECT password FROM logindb WHERE playername=" + name);
        try{
            if(rs.first())
                return rs.getString("password");
        } catch (SQLException e){
        }
        throw new SQLDataException("Error getting " + name + "'s password");
    }
    
    public static String[] getIPs(String name) throws SQLDataException
    {
        String ips = "";
        ResultSet rs = mysql.query("SELECT ips FROM userdb WHERE playername=" + name);
        try{
            if(rs.first())
                ips = rs.getString("ips");
        } catch (SQLException e){
        }
        return ips.split(":");
    }
    
    public static String[] getIPs(String name, String ip) throws SQLDataException
    {
        String[] ips;
        try{
            ips = getIPs(name);
        } catch (SQLDataException e) {
            addIP(name, ip);
            ips = getIPs(name);
        }
        return ips;
    }
    
    public static String[] getNames(String ip, String name) throws SQLDataException
    {
        String[] names;
        try{
            names = getNames(ip);
        } catch (SQLDataException e) {
            addName(ip, name);
            names = getNames(ip);
        }
        return names;
    }
    
    public static String[] getNames(String ip) throws SQLDataException
    {
        String names = "";
        ResultSet rs = mysql.query("SELECT playernames FROM ipdb WHERE ip=" + ip);
        try{
            if(rs.first())
                names = rs.getString("playernames");
        } catch (SQLException e){
        }
        return names.split(":");
    }
    
    public static void setPW(String playername, String newpw){
        mysql.query("UPDATE logindb SET password=" + newpw + " WHERE playername=" + playername);
    }
    
    public static void addName(String ip, String name) throws SQLDataException
    {
        String[] names;
        try {
            names = getNames(name);
        } catch (SQLDataException e)
        {
            names = new String[0];
        }
        String allNames = "";
        for(String nameN: names)
            allNames += nameN + ":";
        if(allNames.contains(name))
            return;
        allNames += ip;
        mysql.query("UPDATE ipdb SET playernames=" + allNames + " WHERE ip=" + ip);
    }
    
    public static void addIP(String name, String ip) throws SQLDataException
    {
        String[] ips;
        try {
            ips = getIPs(name);
        } catch (SQLDataException e) {
            ips = new String[0];
        }
        String allIPs = "";
        for(String ipN: ips)
            allIPs += ipN + ":";
        if(allIPs.contains(ip))
            return;
        allIPs += ip;
        mysql.query("UPDATE userdb SET ips=" + allIPs + " WHERE playername=" + name);
    }
}
