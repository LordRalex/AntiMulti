/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.config;

import com.lordralex.antimulti.AntiMulti;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author Joshua
 */
public class Config {
    
    AntiMulti plugin;
    PluginDescriptionFile pluginInfo;
    public static String pluginName;
    public static String pluginVersion;
    public static String pluginTitle;
    public static String pluginAuthor;
    public static File dataPath;
    public static String messageWhitelist;
    public static String messageIPShared;
    public static boolean enableWhitelist;
    public static boolean enableIPCheck;
    public static boolean enableLogin;
    public static boolean enableReloadKick;
    public static boolean enableProtectPerm;
    public static boolean forceLoginIfData;
    public static boolean fake_online_mode;
    public static String host;
    public static int port;
    public static String user;
    public static String pass;
    public static String database;
    public static int maxUser;
    public static int maxIP;
    public static int timedKick;
    public static int travelDistance;
    
    public Config(AntiMulti aPlugin)
    {
        plugin = aPlugin;
        pluginInfo = plugin.getDescriptionFile();
    }
    
    public void loadConfig() throws IOException
    {
        dataPath = plugin.getFolder();
        FileConfiguration config = plugin.getConfigFile();
        if(!dataPath.exists())
            dataPath.mkdirs();
        messageIPShared = config.getString("messages.shared_ip", "IP used too many times");
        messageWhitelist = config.getString("messages.whitelist", "Whitelist active");
        enableLogin = config.getBoolean("enable.login", false);
        enableIPCheck = config.getBoolean("enable.ip_check", false);
        enableWhitelist = config.getBoolean("enable.whitelist", false);
        enableReloadKick = true;
        maxUser = config.getInt("options.max-names-per-ip", 1);
        maxIP = config.getInt("options.max-ips-per-name", 1);
        timedKick = config.getInt("options.time-kick", 30);
        enableProtectPerm = config.getBoolean("options.perm-protection", true);
        fake_online_mode = config.getBoolean("options.online-mode", Bukkit.getServer().getOnlineMode());
        travelDistance = config.getInt("options.travel-distance", 5);
        host = config.getString("mySQL.host", "127.0.0.1");
        port = config.getInt("mySQL.port", 3306);
        database = config.getString("mySQL.database", "antimulti");
        user = config.getString("mySQL.user", "root");
        pass = config.getString("mySQL.pass", "");
        config.set("messages.shared_ip", messageIPShared);
        config.set("messages.whitelist", messageWhitelist);
        config.set("enable.login", enableLogin);
        config.set("enable.ip_check", enableIPCheck);
        config.set("enable.whitelist", enableWhitelist);
        config.set("options.max-names-per-ip", maxUser);
        config.set("options.max-ips-per-name", maxIP);
        config.set("options.time-kick", timedKick);
        config.set("options.perm-protection", enableProtectPerm);
        config.set("options.online-mode", fake_online_mode);
        config.set("options.travel-distance", travelDistance);
        config.set("mySQL.host", null);
        config.set("mySQL.port", null);
        config.set("mySQL.database", null);
        config.set("mySQL.user", null);
        config.set("mySQL.pass", null);
        config.set("version", pluginInfo.getVersion());
        config.save(dataPath + File.separator + "config.yml");
        if(!Bukkit.getServer().getOnlineMode() && fake_online_mode)
        {
            enableLogin = true;
            enableIPCheck = true;
        }
        messageIPShared = magic(messageIPShared);
        messageWhitelist = magic(messageWhitelist);
    }

    private String magic(String string) {
        string = string.replace("&1", ChatColor.DARK_BLUE + "");
        string = string.replace("&2", ChatColor.DARK_GREEN + "");
        string = string.replace("&3", ChatColor.DARK_AQUA + "");
        string = string.replace("&4", ChatColor.DARK_RED + "");
        string = string.replace("&5", ChatColor.DARK_PURPLE + "");
        string = string.replace("&6", ChatColor.GOLD + "");
        string = string.replace("&7", ChatColor.GRAY + "");
        string = string.replace("&8", ChatColor.DARK_GRAY + "");
        string = string.replace("&9", ChatColor.BLUE + "");
        string = string.replace("&a", ChatColor.GREEN + "");
        string = string.replace("&b", ChatColor.AQUA + "");
        string = string.replace("&c", ChatColor.RED + "");
        string = string.replace("&d", ChatColor.LIGHT_PURPLE + "");
        string = string.replace("&e", ChatColor.YELLOW + "");
        string = string.replace("&f", ChatColor.WHITE + "");
        string = string.replace("&0", ChatColor.BLACK + "");
        return string;
    }
}
