package com.lordralex.antimulti.config;

import com.lordralex.antimulti.logger.AMLogger;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class Configuration {

    private static Plugin plugin;
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
    private static boolean overrideVanillaWL = true;
    private static int moveBuffer = 5;
    private static String sqlHost = "localhost";
    private static String sqlPort = "3306";
    private static String sqlUser = "root";
    private static String sqlPass = "password";
    private static String sqlDB = "database";
    private static boolean sqlEnable = false;

    /**
     * Loads the config for the instance of the AntiMulti plugin passed.
     *
     * @param aP The AntiMulti instance to load the config for
     */
    public static void loadConfig(Plugin aP) {
        plugin = aP;
        FileConfiguration config = plugin.getConfig();
        if (!(new File(plugin.getDataFolder(), "config.yml").exists())) {
            AMLogger.info("No config found, generating default config");
            plugin.saveDefaultConfig();
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
        overrideVanillaWL = config.getBoolean("whitelist.override-vanilla", overrideVanillaWL);
        moveBuffer = config.getInt("move-buffer", moveBuffer);
        sqlEnable = config.getBoolean("mysql.enable", sqlEnable);
        sqlHost = config.getString("mysql.host", sqlHost);
        sqlPort = config.getString("mysql.port", sqlPort);
        sqlUser = config.getString("mysql.user", sqlUser);
        sqlPass = config.getString("mysql.pass", sqlPass);
        sqlDB = config.getString("mysql.db", sqlDB);
    }

    public static String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }

    public static int getPlayerIPLimit() {
        return playerIPLimit;
    }

    public static boolean fakeOnline() {
        return (fakeOnline && !Bukkit.getOnlineMode());
    }

    public static int getPlayerNameLimit() {
        return playerNameLimit;
    }

    public static int getAdminIPLimit() {
        return adminIPLimit;
    }

    public static int getAdminNameLimit() {
        return adminNameLimit;
    }

    public static int getLoginThroddle() {
        return loginThroddle;
    }

    public static String getWhitelistMessage() {
        return whitelistMessage;
    }

    public static String getMaxIPMessage() {
        return maxIPsUsed;
    }

    public static String getMaxNameMessage() {
        return maxNamesUsed;
    }

    public static boolean useIPProtection() {
        return useIPProt;
    }

    public static boolean startWhitelist() {
        return startWhitelist;
    }

    public static boolean overrideVanillaWL() {
        return overrideVanillaWL;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static int getMoveBuffer() {
        return moveBuffer;
    }

    public static void reloadConfig() {
        loadConfig(plugin);
    }

    public static String[] getSQLInfo() {
        return new String[]{
                    sqlHost,
                    sqlPort,
                    sqlUser,
                    sqlPass,
                    sqlDB
                };
    }

    public static boolean useSQL() {
        return sqlEnable;
    }

    private Configuration() {
    }
}
