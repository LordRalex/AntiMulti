package com.lordralex.antimulti.files;

import com.lordralex.antimulti.config.Configuration;
import java.io.IOException;
import org.bukkit.entity.Player;

/**
 * @version 1.0
 * @author Joshua
 */
public class DataManager {

    Manager manager;

    public DataManager() {
        if (Configuration.useSQL()) {
            manager = new SQLManager();
        } else {
            manager = new FlatFileManager();
        }
        manager = manager.setup();
    }

    public String getPassword(String name) {
        if (name == null) {
            return null;
        }
        return manager.getPassword(name);
    }

    public String getPassword(Player player) {
        if (player == null) {
            return null;
        }
        return getPassword(player.getName());
    }

    public void setPassword(String name, String newPW) throws IOException {
        if (name == null) {
            return;
        }
        manager.setPassword(name, newPW);
    }

    public void setPassword(Player player, String newPW) throws IOException {
        if (player == null) {
            return;
        }
        setPassword(player.getName(), newPW);
    }

    public void reload() {
        manager.close();
    }
}
