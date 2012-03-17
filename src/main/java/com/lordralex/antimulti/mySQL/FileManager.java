/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.mySQL;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.loggers.AMLogger;

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
        if(useSQL) {
            SQL.connect();
            AMLogger.info("Connection made to mySQL database");
        }
        else
            AMLogger.info("Cannot connect to the mySQL, using flat-files");
    }
    
    public static String[] getNames(String ip) throws SQLDataException
    {
        if(useSQL)
            return SQL.getNames(ip);
        else
        {
            File fileReader = new File(Config.dataPath + "IP" + File.separator);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(fileReader + ip + ".yml"));
            List<String> list = config.getStringList("names");
            String[] allNames = new String[list.size()];
            for(int i=0; i < allNames.length; i++)
                allNames[i] = list.get(i);
            return allNames;
        }
    }
    
    public static String[] getNames(InetAddress ip) throws SQLDataException
    {
        return getNames(ip.toString());
    }
    
    public static String[] getNames(InetSocketAddress address) throws SQLDataException
    {
        return getNames(address.getAddress());
    }
    
    public static String[] getNames(Player player) throws SQLDataException
    {
        return getNames(player.getAddress());
    }
    
    public static String[] getIPs(String name) throws SQLDataException
    {
        if(useSQL)
            return SQL.getIPs(name);
        else
        {
            File fileReader = new File(Config.dataPath + "names" + File.separator);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(fileReader + name + ".yml"));
            String list1 = config.getString("original");
            List<String> list2 = config.getStringList("alts");
            String[] allIPs = new String[1 + list2.size()];
            allIPs[0] = list1;
            for(int i=1; i < allIPs.length; i++)
                allIPs[i] = list2.get(i);
            return allIPs;
        }
    }
    
    public static String[] getIPs(Player player) throws SQLDataException
    {
        return getIPs(player.getName());
    }
    
    public static String getPW(String player) throws SQLDataException, NoSuchAlgorithmException
    {
        if(useSQL)
            return SQL.getPW(player);
        else
        {
            File fileReader = new File(Config.dataPath + "login" + File.separator);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(fileReader + player + ".yml"));
            return config.getString("password", Encoder.encode("None"));
        }
    }
    
    public static String getPW(Player player) throws SQLDataException, NoSuchAlgorithmException
    {
        return getPW(player.getName());
    }
    
    public static String getPW(AMPlayer player) throws SQLDataException, NoSuchAlgorithmException
    {
        return getPW(player.getName());
    }
    
    private void saveConfig(FileConfiguration config, File file) throws IOException
    {
        if(config == null || file == null)
            return;
        if(!file.exists())
            file.mkdirs();
        config.save(file);
    }
}
