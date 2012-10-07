package com.lordralex.antimulti.config;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.logger.AMLogger;
import com.lordralex.antimulti.utils.Formatter;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.2
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
    private static boolean overrideVanillaWL = true;
    private static int moveBuffer = 5;
    private static String sqlHost = "localhost";
    private static String sqlPort = "3306";
    private static String sqlUser = "root";
    private static String sqlPass = "password";
    private static String sqlDB = "database";
    private static boolean sqlEnable = false;

    /**
     * Loads the config for the instance of the AntiMulti plugin passed. This
     * also checks to see if the current config is up to date and loads the
     * values to the RAM for fast access later.
     *
     * @param aP The AntiMulti instance to load the config for
     */
    public static void loadConfig(AntiMulti aP) {
        plugin = aP;
        FileConfiguration config = plugin.getConfig();
        if (!(new File(plugin.getDataFolder(), "config.yml").exists())) {
            AMLogger.info("No config found, generating default config");
            plugin.saveDefaultConfig();
        }
        if (config.getString("version") == null || !config.getString("version", plugin.getDescription().getVersion()).equalsIgnoreCase(plugin.getDescription().getVersion())) {
            AMLogger.info("Older config version detected, updating config to new version");
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
        overrideVanillaWL = config.getBoolean("whitelist.override-vanilla", overrideVanillaWL);
        moveBuffer = config.getInt("move-buffer", moveBuffer);
        sqlEnable = config.getBoolean("mysql.enable", sqlEnable);
        sqlHost = config.getString("mysql.host", sqlHost);
        sqlPort = config.getString("mysql.port", sqlPort);
        sqlUser = config.getString("mysql.user", sqlUser);
        sqlPass = config.getString("mysql.pass", sqlPass);
        sqlDB = config.getString("mysql.db", sqlDB);

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
        config.set("whitelist.override-vanilla", overrideVanillaWL);
        config.set("move-buffer", moveBuffer);
        config.set("mysql.enable", sqlEnable);
        config.set("mysql.host", sqlHost);
        config.set("mysql.port", sqlPort);
        config.set("mysql.user", sqlUser);
        config.set("mysql.pass", sqlPass);
        config.set("mysql.db", sqlDB);
        config.set("version", plugin.getDescription().getVersion());
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            AMLogger.error(ex, "Error saving AntiMulti config");
        }

        if (fakeOnline && !Bukkit.getOnlineMode()) {
            useIPProt = true;
        }
        whitelistMessage = Formatter.handleColorCodes(whitelistMessage);
        maxIPsUsed = Formatter.handleColorCodes(maxIPsUsed);
        maxNamesUsed = Formatter.handleColorCodes(maxNamesUsed);
    }

    private static void convert(FileConfiguration config) {
        AMLogger.info("Going to try to update your config from " + config.getString("version", "an unknown version (will assume 2.0.2)") + " to " + plugin.getDescription().getVersion());
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
        config.set("version", plugin.getDescription().getVersion());
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            AMLogger.error(ex, "Error saving AntiMulti config");
        }
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

    public static AntiMulti getPlugin() {
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
