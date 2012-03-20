/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.data;

import com.lordralex.antimulti.mySQL.Encoder;
import com.lordralex.antimulti.mySQL.FileManager;
import java.io.IOException;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Joshua
 */
public class AMPlayer {
    
    public Player player;
    private String password;
    public boolean loggedIn;
    public Location loginSpot;
    
    public AMPlayer()
    {
        player = null;
        password = null;
        loggedIn = false;
        loginSpot = null;
    }
    
    public AMPlayer(Player person)
    {
        player = person;
        password = "None";
        loginSpot = person.getLocation();
        loggedIn = false;
    }
    
    public AMPlayer(Player person, String pass)
    {
        player = person;
        password = pass;
        loginSpot = person.getLocation();
        loggedIn = false;
    }
    
    public String getName()
    {
        return player.getName();
    }
    
    public InetAddress getAddress()
    {
        return player.getAddress().getAddress();
    }
    
    public boolean login(String password) throws NoSuchAlgorithmException
    {
        if(loggedIn)
            return true;
        loggedIn = Encoder.areEqual(this.password, password);
        return loggedIn;
    }
    
    public void setLogin(boolean income)
    {
        loggedIn = income;
    }
    
    public boolean setPassword(String newP, String newP2) throws NoSuchAlgorithmException, IOException
    {
        if(!newP.equals(newP2))
            return false;
        password = Encoder.encode(newP);
        FileManager.setPW(player.getName(), password);
        return true;
    }
}
