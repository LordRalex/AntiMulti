/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.data.Searcher;
/**
 *
 * @author Joshua
 */
public class AntiMulti extends JavaPlugin{
        
    @Override
    public void onEnable()
    {
        setUpClasses();
    }
    
    @Override
    public void onDisable()
    {
        
    }
    
    private void setUpClasses()
    {
        new Searcher(this);
        new Config(this);
    }
    
    public File getFolder() {
        return this.getDataFolder();
    }
    
    public FileConfiguration getConfigFile(){
        return this.getConfig();
    }

    public PluginDescriptionFile getDescriptionFile() {
        return this.getDescription();
    }
}
