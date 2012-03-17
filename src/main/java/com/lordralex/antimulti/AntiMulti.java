/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti;

import com.lordralex.antimulti.config.Config;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.data.Searcher;
import com.lordralex.antimulti.listeners.BlockListener;
import com.lordralex.antimulti.listeners.CommandListener;
import com.lordralex.antimulti.listeners.PlayerListener;
import java.io.File;
import java.util.ArrayList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
/**
 *
 * @author Joshua
 */
public class AntiMulti extends JavaPlugin{
    
    ArrayList<AMPlayer> playersOnServer = new ArrayList<AMPlayer>();
    PlayerListener pListener;
    CommandListener cListener;
    BlockListener bListener;
        
    @Override
    public void onEnable()
    {
        setUpListeners();
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
    
    private void setUpListeners()
    {
        pListener = new PlayerListener(this);
        bListener = new BlockListener(this);
        cListener = new CommandListener(this);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(bListener, this);
        pm.registerEvents(pListener, this);
        for(String cmd: cListener.cmds)
        {
            getCommand(cmd).setExecutor(cListener);
        }
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
