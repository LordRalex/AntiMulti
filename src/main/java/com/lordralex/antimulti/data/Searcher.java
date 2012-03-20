/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.data;

import org.bukkit.entity.Player;
import com.lordralex.antimulti.AntiMulti;
import org.bukkit.Bukkit;

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
        for(AMPlayer possible: plugin.playersOnServer)
            if(possible.player.equals(player))
                return possible;
        return findPlayer(player.getName());
    }   
    
    public static AMPlayer findPlayer(String name)
    {
        Player player = Bukkit.getServer().getPlayerExact(name);
        if(player == null)
            return null;
        return findPlayer(player);
    }
}
