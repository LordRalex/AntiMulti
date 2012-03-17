/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import com.lordralex.antimulti.data.AMPlayer;
import com.lordralex.antimulti.data.Searcher;

/**
 *
 * @author Joshua
 */
public class BlockListener implements Listener{
    
    @EventHandler (priority=EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event)
    {
        AMPlayer user = Searcher.findPlayer(event.getPlayer());
        if(user == null || !user.loggedIn)
            event.setCancelled(true);
    }
    
    @EventHandler (priority=EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        AMPlayer user = Searcher.findPlayer(event.getPlayer());
        if(user == null || !user.loggedIn)
            event.setCancelled(true);
    }
    
}
