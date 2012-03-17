/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import lib.PatPeter.SQLibrary.MySQL;
import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.loggers.AMLogger;

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
    
    private static void doesDBExist()
    {
        if(!mysql.checkTable("ipdb"))
            mysql.query("CREATE TABLE ipdb(ip, playernames);");
        if(!mysql.checkTable("logindb"))
            mysql.query("CREATE TABLE logindb(playername, password);");
        if(!mysql.checkTable("userdb"))
            mysql.query("CREATE TABLE userdb(playername, ips);");
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
                ips = rs.getString("password");
        } catch (SQLException e){
        }
        if(ips == null || ips.equals(""))
            throw new SQLDataException("Error getting " + name + "'s ip list");
        return ips.split(":");
    }
    
    public static String[] getNames(String ip) throws SQLDataException
    {
        String names = "";
        ResultSet rs = mysql.query("SELECT playernames FROM ipdb WHERE ip=" + ip);
        try{
            if(rs.first())
                names = rs.getString("password");
        } catch (SQLException e){
        }
        if(names == null || names.equals(""))
            throw new SQLDataException("Error getting " + ip + "'s name list");
        return names.split(":");
    }
    
    public static void setPW(String playername, String newpw){
        mysql.query("UPDATE logindb SET password=" + newpw + " WHERE playername=" + playername);
    }
    
    public static void addName(String ip, String name) throws SQLDataException
    {
        String[] names = getNames(name);
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
        String[] ips = getIPs(name);
        String allIPs = "";
        for(String ipN: ips)
            allIPs += ipN + ":";
        if(allIPs.equals("") || allIPs.equals(":"))
            throw new SQLDataException("Error resolving existing IPs");
        if(allIPs.contains(ip))
            return;
        allIPs += ip;
        mysql.query("UPDATE userdb SET ips=" + allIPs + " WHERE playername=" + name);
    }
}
