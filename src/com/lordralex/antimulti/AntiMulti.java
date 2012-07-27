package com.lordralex.antimulti;

import com.lordralex.antimulti.command.CommandManager;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.listener.PlayerListener;
import com.lordralex.antimulti.logger.AMLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 1.2
 * @author Joshua
 * @since 1.0
 */
public class AntiMulti extends JavaPlugin {

    public static PlayerListener pListener;
    updateThread update = null;

    @Override
    public void onLoad() {
        String currentVersion = this.getDescription().getVersion();
        AMLogger.info("Loading AntiMulti " + currentVersion);
        try {
            update = new updateThread(currentVersion);
            update.start();
        } catch (Exception ex) {
            AMLogger.warning("[AM] Could not check for an update");
        }
    }

    @Override
    public void onEnable() {
        try {
            AMLogger.info("Enabling AntiMulti");
            AMLogger.info("Server info for error purposes: " + Bukkit.getBukkitVersion());
            Configuration.loadConfig(this);
            pListener = new PlayerListener(this);
            Bukkit.getPluginManager().registerEvents(pListener, this);
            CommandManager.setup(this);
            if (Configuration.fakeOnline() && !Bukkit.getOnlineMode()) {
                AMLogger.info("USING FAKE ONLINE MODE");
            }
            AMLogger.info("[AM] " + this.getName() + "-" + this.getVersion() + " successfully enabled");
            if (!update.isAlive()) {
                String[] updateMessage = update.getUpdate();
                if (updateMessage != null && updateMessage[0] != null) {
                    AMLogger.info(updateMessage[0]);
                    if (updateMessage[1] != null) {
                        AMLogger.info(updateMessage[1]);
                    }
                    if (updateMessage[2] != null) {
                        AMLogger.info(updateMessage[2]);
                    }
                }
            }
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

        String currentVersion;
        URL versionOnline;
        String[] update = new String[3];

        public updateThread(String version) throws Exception {
            versionOnline = new URL("https://raw.github.com/LordRalex/AntiMulti/master/version.txt");
            currentVersion = version;
            update[0] = update[1] = update[2] = null;
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
                    update[0] = "[AM] Current version: " + currentVersion;
                    update[1] = "[AM] An update is available: " + line;
                }
                if ((line = in.readLine()) != null) {
                    update[2] = "[AM] Update priority: " + line;
                }
                in.close();
            } catch (IOException e) {
                update[0] = "[AM] Could not check for an update";
            } catch (Exception e) {
                update[0] = "Error occured while checking for an update";
            }
        }

        public String[] getUpdate() {
            return update;
        }
    }
}
