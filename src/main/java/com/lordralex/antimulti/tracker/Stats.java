/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.tracker;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.tracker.Metrics.Graph;
import java.io.IOException;
import org.bukkit.Bukkit;

/**
 *
 * @author Joshua
 */
public class Stats {
    
    Metrics metrics;
    AntiMulti plugin;
    
    public Stats(AntiMulti aPlugin) throws IOException
    {
        plugin = aPlugin;
        metrics = new Metrics(plugin);
        Graph graph = metrics.createGraph("Number of players registered");
        graph.addPlotter(new Metrics.Plotter("Online/Offline servers") {

            @Override
            public int getValue() {
                //0 = online, 1 = offline
                if(Bukkit.getServer().getOnlineMode())
                    return 0;
                else
                    return 1;
            }
        });
        graph.addPlotter(new Metrics.Plotter("Number of players") {

            @Override
            public int getValue() {
                return Bukkit.getServer().getOnlinePlayers().length;
            }
        });
        metrics.start();
    }    
}
