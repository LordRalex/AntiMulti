/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.data;

import com.lordralex.antimulti.AntiMulti;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author Joshua
 */
public class Searcher {
    
    private static AntiMulti plugin;
    
    public Searcher(AntiMulti aPlugin){
        plugin = aPlugin;
    }
    
    public static AMPlayer findPlayer(Player player)
    {
        return findPlayer(player.getName());
    }   
    
    public static AMPlayer findPlayer(String name)
    {
        for(AMPlayer possible: plugin.playersOnServer)
            if(possible.player.getName().equals(name))
                return possible;
        return null;
    }
}
