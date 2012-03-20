/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.data.AMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Joshua
 */
public class PlayerListener implements Listener{
    
    AntiMulti plugin;

    public PlayerListener(AntiMulti aThis) {
        plugin = aThis;
    }
    
    @EventHandler (priority=EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if(player != null) {
            for(AMPlayer amplayer: plugin.playersOnServer)
            {
                if(amplayer != null)
                {
                    if(player.equals(amplayer.player))
                    {
                        if(!amplayer.loggedIn)
                            event.setQuitMessage(null);
                        amplayer = null;
                    }
                }
            }
        }
    }
    
    @EventHandler (priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);
    }
}
