/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.mySQL;

import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.loggers.AMLogger;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Joshua
 */
public class FileManager {
    
    static boolean useSQL;
    
    public static void testConnection()
    {
        useSQL = SQL.testConnection();
    }
    
    public static void openConnection()
    {
        testConnection();
        if(useSQL) {
            SQL.connect();
            AMLogger.info("Connection made to mySQL database");
        }
        else
        {
            AMLogger.info("Cannot connect to the mySQL, using flat-files");
            if(!Config.dataPath.exists())
                Config.dataPath.mkdirs();
            File login = new File(Config.dataPath + File.separator + "login" + File.separator);
            File ip = new File(Config.dataPath + File.separator + "IP" + File.separator);
            File name = new File(Config.dataPath + File.separator + "name" + File.separator);
            if(!login.exists())
                login.mkdirs();
            if(!ip.exists())
                ip.mkdirs();
            if(!name.exists())
                name.mkdirs();
        }
    }
    
    public static void closeConnection()
    {
        if(useSQL)
        {
            SQL.disconnect();
        }
    }
    
    public static String[] getNames(Player player) throws SQLDataException, IOException
    {
        String name = player.getName();
        String ip = player.getAddress().getAddress().getHostAddress();
        if(useSQL)
            return SQL.getIPs(name);
        else
        {
            File fileReader = new File(Config.dataPath + File.separator  + "IP" + File.separator);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(fileReader + File.separator + ip + ".yml"));
            if(!(new File(fileReader + File.separator + ip + ".yml").exists()))
            {
                ArrayList list = new ArrayList();
                list.add(player.getName());
                config.set("names", list.listIterator());
                config.save(new File(fileReader + File.separator + ip + ".yml"));
            }
            List<String> list2 = config.getStringList("names");
            String[] allIPs = new String[list2.size()];
            for(int i=0; i < allIPs.length; i++)
                allIPs[i] = list2.get(i);
            return allIPs;
        }
    }
    
    public static String[] getNames(String name) throws SQLDataException, IOException
    {
        Player player = Bukkit.getServer().getPlayerExact(name);
        return getNames(player);
    }
    
    public static String[] getIPs(String name) throws SQLDataException, IOException
    {
        Player player = Bukkit.getServer().getPlayerExact(name);
        return getIPs(player);
    }
    
    public static String[] getIPs(Player player) throws SQLDataException, IOException
    {
        String name = player.getName();
        if(useSQL)
            return SQL.getIPs(name);
        else
        {
            File fileReader = new File(Config.dataPath + File.separator + "name" + File.separator);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(fileReader + File.separator + name + ".yml"));
            if(!(new File(fileReader + File.separator + name + ".yml").exists()))
            {
                ArrayList<String> names = new ArrayList<String>();names.add("none");
                config.set("original", player.getAddress().getAddress().getHostAddress());
                config.set("alts", new ArrayList());
                config.save(new File(fileReader + File.separator + name + ".yml"));
            }
            String list1 = config.getString("original");
            List<String> list2 = config.getStringList("alts");
            String[] allIPs = new String[1 + list2.size()];
            allIPs[0] = list1;
            for(int i=1; i < allIPs.length; i++)
                allIPs[i] = list2.get(i);
            return allIPs;
        }
    }
    
    public static String getPW(String player) throws SQLDataException, NoSuchAlgorithmException, IOException
    {
        if(useSQL)
            return SQL.getPW(player);
        else
        {
            File fileReader = new File(Config.dataPath + File.separator  + "login" + File.separator);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(fileReader + File.separator + player + ".yml"));
            if(!(new File(fileReader + File.separator + player + ".yml").exists()))
            {
                config.set("password", Encoder.encode("None"));
                config.save(new File(fileReader + File.separator + player + ".yml"));
            }
            return config.getString("password", Encoder.encode("None"));
        }
    }
    
    public static String getPW(Player player) throws SQLDataException, NoSuchAlgorithmException, IOException
    {
        return getPW(player.getName());
    }
    
    public static String getPW(AMPlayer player) throws SQLDataException, NoSuchAlgorithmException, IOException
    {
        return getPW(player.getName());
    }
    
    public static void setPW(String name, String newP) throws IOException, NoSuchAlgorithmException
    {
        if(useSQL)
            SQL.setPW(name, newP);
        else
        {
            File fileReader = new File(Config.dataPath + File.separator  + "login" + File.separator);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(fileReader + File.separator + name + ".yml"));
            config.set("password", newP);
            config.save(new File(fileReader + File.separator + name + ".yml"));
        }
    }
    
    public static void addName(String IP, String name) throws IOException, SQLDataException
    {
        if(useSQL)
            SQL.addName(IP, name);
        else
        {
            File fileReader = new File(Config.dataPath + File.separator  + "name" + File.separator + name + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(fileReader);
            config.set("name", config.getStringList("name"));
            config.save(fileReader);
        }
    }
    
    public static void addName(byte[] ip, String name) throws IOException, SQLDataException
    {
        String newIP = "";
        for(int i=0; i < ip.length; i++)
            newIP += ip[i];
        addName(newIP, name);
    }
    
    public static void addName(Player player) throws IOException, SQLDataException
    {
        addName(player.getAddress().getAddress().getAddress(), player.getName());
    }
    
    public static void addIP(Player player) throws IOException, SQLDataException
    {
        addIP(player.getName(), player.getAddress().getAddress().getAddress());
    }
    
    public static void addIP(String name, byte[] ip) throws IOException, SQLDataException
    {
        String newIP = "";
        for(int i=0; i < ip.length; i++)
            newIP += ip[i];
        addIP(name, newIP);
    }
    
    public static void addIP(String name, String IP) throws IOException, SQLDataException
    {
        if(useSQL)
            SQL.addIP(name, IP);
        else
        {
            File fileReader = new File(Config.dataPath + File.separator  + "name" + File.separator + IP + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(fileReader);
            if(fileReader.exists())
            {
                config.set("original", IP);
            }
            else
            {
                config.set("original", config.getString("original"));
                List<String> list = config.getStringList("alts");
                ArrayList<String> newOnes = new ArrayList<String>();
                newOnes.addAll(list);
                newOnes.add(IP);
                config.set("alts", newOnes);
                config.save(fileReader);
            }
        }
    }
}
