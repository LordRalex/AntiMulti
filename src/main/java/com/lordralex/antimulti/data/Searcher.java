/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.data;

import org.bukkit.entity.Player;
import com.lordralex.antimulti.AntiMulti;

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
        AMPlayer found = null;
        for(AMPlayer possible: plugin.playersOnServer)
        {
            if(possible != null && name.equals(possible.getName()))
                found = possible;
        }
        return found;
    }
}
