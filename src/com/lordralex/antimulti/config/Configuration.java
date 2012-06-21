package com.lordralex.antimulti.config;

import com.lordralex.antimulti.AntiMulti;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @version 1.0
 * @author Joshua
 */
public class Configuration {

    private static AntiMulti plugin;
    private static int playerIPLimit = 2;
    private static int playerNameLimit = 2;
    private static int adminIPLimit = 1;
    private static int adminNameLimit = 1;
    private static int loginThroddle = 5;
    private static String whitelistMessage = "You are not whitelisted";
    private static String maxIPsUsed = "Too many IPs used";
    private static String maxNamesUsed = "Too many names used";
    private static boolean useIPProt = true;
    private static boolean fakeOnline = true;
    private static boolean startWhitelist = false;
    private static Logger log;

    public static void loadConfig(AntiMulti aP) {
        plugin = aP;
        log = plugin.getLogger();
        FileConfiguration config = plugin.getConfig();
        if (!(new File(aP.getDataFolder(), "config.yml").exists())) {
            plugin.saveDefaultConfig();
        }
        if (config.getString("version") == null || !config.getString("version", plugin.getDescription().getVersion()).equalsIgnoreCase(plugin.getDescription().getVersion())) {
            plugin.getLogger().info("Older config version detected, updating config to new version");
            convert(config);
        }

        playerIPLimit = config.getInt("limits.member.IP", playerIPLimit);
        playerNameLimit = config.getInt("limits.member.name", playerNameLimit);
        adminIPLimit = config.getInt("limits.admin.IP", adminIPLimit);
        adminNameLimit = config.getInt("limits.admin.name", adminNameLimit);
        loginThroddle = config.getInt("throddle", loginThroddle);
        whitelistMessage = config.getString("messages.whitelist", whitelistMessage);
        maxIPsUsed = config.getString("messages.max.ip", maxIPsUsed);
        maxNamesUsed = config.getString("messages.max.name", maxNamesUsed);
        useIPProt = config.getBoolean("protection.ip", useIPProt);
        fakeOnline = config.getBoolean("fake-online", fakeOnline);
        startWhitelist = config.getBoolean("start.whitelist", startWhitelist);

        config.set("limits.member.IP", playerIPLimit);
        config.set("limits.member.name", playerNameLimit);
        config.set("limits.admin.IP", adminIPLimit);
        config.set("limits.admin.name", adminNameLimit);
        config.set("throddle", loginThroddle);
        config.set("messages.whitelist", whitelistMessage);
        config.set("messages.max.ip", maxIPsUsed);
        config.set("messages.max.name", maxNamesUsed);
        config.set("protection.ip", useIPProt);
        config.set("fake-online", fakeOnline);
        config.set("start.whitelist", startWhitelist);
        config.set("version", plugin.getDescription().getVersion());
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Error saving AntiMulti config", ex);
        }
    }

    public static void convert(FileConfiguration config) {
        log.info("Going to try to update your config from " + config.getString("version", "an unknown version (will assume 2.0.2)") + " to " + plugin.getDescription().getVersion());
        String oldversion = config.getString("version", "2.0.2").toLowerCase().trim();
        if (oldversion.equalsIgnoreCase("2.0.2")) {
            loginThroddle = config.getInt("logins-per-second", loginThroddle);
            playerIPLimit = config.getInt("max-ips", playerIPLimit);
            playerNameLimit = config.getInt("max-names", playerNameLimit);
            adminIPLimit = config.getInt("max-admin-ips", adminIPLimit);
            adminNameLimit = config.getInt("max-admin-names", adminNameLimit);
        }

        config.set("limits.member.IP", playerIPLimit);
        config.set("limits.member.name", playerNameLimit);
        config.set("limits.admin.IP", adminIPLimit);
        config.set("limits.admin.name", adminNameLimit);
        config.set("throddle", loginThroddle);
        config.set("messages.whitelist", whitelistMessage);
        config.set("messages.max.ip", maxIPsUsed);
        config.set("messages.max.name", maxNamesUsed);
        config.set("protection.ip", useIPProt);
        config.set("fake-online", fakeOnline);
        config.set("start.whitelist", startWhitelist);
        config.set("version", plugin.getDescription().getVersion());
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error saving AntiMulti config", ex);
        }
    }

    public static int getPlayerIPLimit()
    {
        return playerIPLimit;
    }

    public static int getPlayerNameLimit()
    {
        return playerNameLimit;
    }

    public static int getAdminIPLimit()
    {
        return adminIPLimit;
    }

    public static int getAdminNameLimit()
    {
        return adminNameLimit;
    }

    public static int getLoginThroddle()
    {
        return loginThroddle;
    }

    public static String getWhitelistMessage()
    {
        return whitelistMessage;
    }

    public static String getMaxIPMessage()
    {
        return maxIPsUsed;
    }

    public static String getMaxNameMessage()
    {
        return maxNamesUsed;
    }

    public static boolean useIPProtection()
    {
        return useIPProt;
    }

    public static boolean startWhitelist()
    {
        return startWhitelist;
    }
}
