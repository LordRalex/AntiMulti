/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.data;

import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.lordralex.antimulti.mySQL.Encoder;
import com.lordralex.antimulti.mySQL.SQL;

/**
 *
 * @author Joshua
 */
public class AMPlayer {
    
    Player player;
    private String password;
    int id;
    boolean loggedIn;
    Location loginSpot;
    
    public AMPlayer()
    {
        player = null;
        password = null;
        id = -1;
        loggedIn = false;
        loginSpot = null;
    }
    
    public AMPlayer(Player person, int id)
    {
        player = person;
        this.id = id;
        password = "None";
        loginSpot = person.getLocation();
        loggedIn = false;
    }
    
    public AMPlayer(Player person, int id, String pass)
    {
        player = person;
        this.id = id;
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
    
    public boolean setPassword(String newP, String newP2) throws NoSuchAlgorithmException
    {
        if(!newP.equals(newP2))
            return false;
        String newPass = Encoder.encode(newP);
        if(!newPass.equals(password))
            return false;
        password = newPass;
        SQL.setPW(player.getName(), password);
        return true;
    }
}
