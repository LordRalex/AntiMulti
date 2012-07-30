package com.lordralex.antimulti.files;

import com.lordralex.antimulti.config.Configuration;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Joshua
 */
public class FlatFileManager implements Manager {

    File ipFolder, passFolder;

    public FlatFileManager() {
    }

    @Override
    public String getPassword(String name) {
        File temp = new File(passFolder, name + ".yml");
        FileConfiguration user = YamlConfiguration.loadConfiguration(temp);
        String password = user.getString("password", "");
        return password;
    }

    @Override
    public void setPassword(String name, String newPass) throws IOException {
        File temp = new File(passFolder, name + ".yml");
        FileConfiguration user = YamlConfiguration.loadConfiguration(temp);
        user.set("password", newPass);
        user.save(temp);
    }

    @Override
    public Manager setup() {
        passFolder = new File(Configuration.getPlugin().getUserFolder(), "passwords");
        ipFolder = new File(Configuration.getPlugin().getUserFolder(), "ip");
        return this;
    }

    @Override
    public void close() {
    }
}
