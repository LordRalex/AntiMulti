package com.lordralex.antimulti;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.encryption.Encrypt;
import com.lordralex.antimulti.listener.PlayerListener;
import com.lordralex.antimulti.logger.AMLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 1.2
 * @author Joshua
 * @since 1.0
 */
public class AntiMulti extends JavaPlugin {

    public static Permission perms = null;
    public static PlayerListener pListener;

    @Override
    public void onLoad() {
        //if (!(new File(getDataFolder(), "logs")).exists()) {
        //    new File(getDataFolder(), "logs").mkdirs();
        //}
        //AMLogger.setup(this);
        String currentVersion = this.getDescription().getVersion();
        AMLogger.info("Loading AntiMulti " + currentVersion);
        try {
            updateThread update = new updateThread(currentVersion);
            update.start();
        } catch (Exception ex) {
            AMLogger.warning("[AM] Could not check for an update");
        }
    }

    @Override
    public void onEnable() {
        try {
            System.out.println("The encrypted NONE is: " + Encrypt.encrypt("None"));
            AMLogger.info("Enabling AntiMulti");
            AMLogger.info("Server info for error purposes: " + Bukkit.getBukkitVersion());
            Configuration.loadConfig(this);
            if (getServer().getPluginManager().isPluginEnabled("Vault")) {
                perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
            } else {
                perms = null;
            }
            if (perms == null) {
                AMLogger.info("[AM] Vault not found, using SuperPerms, which is not really of much concern to you.");
                AMLogger.info("[AM] While I prefer you used Vault, I can work with this.");
                AMLogger.info("[AM] Your permissions should be fine anyhow.");
            } else {
                AMLogger.info("[AM] Using Vault for your permissions handler.");
                AMLogger.info("[AM] This means any permissions plugin you have will be fine, usually.");
            }
            pListener = new PlayerListener(this);
            Bukkit.getPluginManager().registerEvents(pListener, this);
            CommandManager.setup(this);
            if (Configuration.fakeOnline() && !Bukkit.getOnlineMode()) {
                AMLogger.info("USING FAKE ONLINE MODE");
            }
            AMLogger.info("[AM] " + this.getName() + "-" + this.getVersion() + " successfully enabled");
        } catch (Throwable ex) {
            AMLogger.error(ex, "[AM] An error occurred on startup, disabling " + this.getName());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            Bukkit.getScheduler().cancelTasks(this);
        } catch (Exception e) {
            AMLogger.error(e);
        }
        AMLogger.close();
        this.getLogger().info("Disabled " + getVersion());
    }

    public File getUserFolder() {
        return new File(getDataFolder(), "userData");
    }

    public String getVersion() {
        return this.getDescription().getVersion();
    }

    public boolean checkForUpdate() {
        return false;
    }

    private class updateThread extends Thread {

        String currentVersion = "2.0.4";
        URL versionOnline;

        public updateThread(String version) throws Exception {
            versionOnline = new URL("https://raw.github.com/LordRalex/AntiMulti/master/version.txt");
            currentVersion = version;
        }

        @Override
        public void run() {
            String line = null;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(versionOnline.openStream()));
                while (line == null || line.equalsIgnoreCase("")) {
                    line = in.readLine();
                }
                if (!line.equalsIgnoreCase(currentVersion)) {
                    AMLogger.info("[AM] Current version: " + currentVersion);
                    AMLogger.info("[AM] An update is available: " + line);
                }
                if ((line = in.readLine()) != null) {
                    AMLogger.info("[AM] Update priority: " + line);
                }
                in.close();
            } catch (IOException e) {
                AMLogger.warning("[AM] Could not check for an update");
            } catch (Exception e) {
                AMLogger.error(e, "Error occured while checking for an update");
            }
        }
    }
}
