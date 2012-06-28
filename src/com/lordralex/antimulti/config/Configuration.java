package com.lordralex.antimulti.config;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.logger.AMLogger;
import com.lordralex.antimulti.utils.Formatter;
import java.io.File;
import java.io.IOException;
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
    private static boolean loginUse = false;
    private static boolean playerLogin = false;
    private static boolean adminLogin = true;
    private static int moveBuffer = 5;

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
        loginUse = config.getBoolean("protection.password", loginUse);
        playerLogin = config.getBoolean("require-login.player", playerLogin);
        adminLogin = config.getBoolean("require-login.admin", adminLogin);
        fakeOnline = config.getBoolean("fake-online", fakeOnline);
        startWhitelist = config.getBoolean("start.whitelist", startWhitelist);
        overrideVanillaWL = config.getBoolean("whitelist.override-vanilla", overrideVanillaWL);
        moveBuffer = config.getInt("move-buffer", moveBuffer);

        config.set("limits.member.IP", playerIPLimit);
        config.set("limits.member.name", playerNameLimit);
        config.set("limits.admin.IP", adminIPLimit);
        config.set("limits.admin.name", adminNameLimit);
        config.set("throddle", loginThroddle);
        config.set("messages.whitelist", whitelistMessage);
        config.set("messages.max.ip", maxIPsUsed);
        config.set("messages.max.name", maxNamesUsed);
        config.set("protection.ip", useIPProt);
        config.set("protection.password", loginUse);
        config.set("require-login.player", playerLogin);
        config.set("require-login.admin", adminLogin);
        config.set("fake-online", fakeOnline);
        config.set("start.whitelist", startWhitelist);
        config.set("whitelist.override-vanilla", overrideVanillaWL);
        config.set("move-buffer", moveBuffer);
        config.set("version", plugin.getDescription().getVersion());
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            AMLogger.error(ex, "Error saving AntiMulti config");
        }

        if(fakeOnline)
        {
            useIPProt = true;
            loginUse = true;
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

    public static int getPlayerIPLimit() {
        return playerIPLimit;
    }

    public static boolean fakeOnline() {
        return fakeOnline;
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

    public static boolean useLoginProtection() {
        return loginUse;
    }

    public static boolean requirePlayerLogin() {
        return playerLogin;
    }

    public static boolean requireAdminLogin() {
        return adminLogin;
    }

    public static int getMoveBuffer()
    {
        return moveBuffer;
    }

    public static void reloadConfig() {
        loadConfig(plugin);
    }
}
