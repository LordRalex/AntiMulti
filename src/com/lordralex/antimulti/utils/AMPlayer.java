package com.lordralex.antimulti.utils;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.encryption.AlgorithmException;
import com.lordralex.antimulti.encryption.Encrypt;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This represents the plugin's player. This contains the data needed for the
 * plugin which is found using the name of the player, and stores their login
 * status and the encrypted password.
 *
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class AMPlayer {

    private final String name;
    private String password;
    private boolean loggedIn;
    private Location loginLoc;

    /**
     * Creates a new AMPlayer using the data sent using the player object.
     *
     * @param player
     */
    public AMPlayer(Player player) {
        name = player.getName().toLowerCase();
        loginLoc = player.getLocation();
        password = AntiMulti.getPlugin().getManager().getPassword(name);
        loggedIn = false;
    }

    /**
     * This returns the name of the AMPlayer. This uses the player's name and
     * should not change from the player's true name unless that was changed
     * through unknown means
     *
     * @return The AMPlayer's name
     */
    public String getName() {
        return name;
    }

    /**
     * This gets the location the player logged in at. This is not always
     * accurate if the AMPlayer was created after the player logged in, in which
     * case this returns the location of that player when it was created.
     *
     * @return The location stored
     */
    public Location getLoginLocation() {
        return loginLoc;
    }

    /**
     * This checks to see if a password matches the encrypted password stored.
     * This can use either the raw data to check or an encrypted version
     *
     * @param test The password to check against the encrypted password
     * @return True if the password matches, otherwise false.
     */
    public boolean checkPW(String test) {
        return (Encrypt.areEqual(test, password));
    }

    /**
     * This tells if a player is logged in, regardless of how they were logged
     * in.
     *
     * @return True is the player was logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * This sets the login status of the player to true then return the value of
     * the login value.
     *
     * @return The login status, which is always true.
     */
    public boolean logIn() {
        loggedIn = true;
        return loggedIn;
    }

    /**
     * This checks to see if the password was correct and logs them in if so. If
     * the password does not match and the player was not already logged in,
     * this will return false. If the player was logged in already or the
     * password was right, this returns true.
     *
     * @param pw The password to check against the stored password
     * @return True if the player was logged in, false otherwise
     */
    public boolean logIn(String pw) {
        if (checkPW(pw)) {
            loggedIn = true;
        }
        return loggedIn;
    }

    /**
     * This changes the password. This first will check to see if the password
     * matches, and if they are equal, changes it to the new password. If an
     * error occurs while saving the password or encrypting the password, then
     * this will restore the password and return false
     *
     * @param oldPW The old password on record
     * @param newPW The new password to store
     * @return True if the password could be and was changed, otherwise false.
     */
    public boolean changePW(String oldPW, String newPW) {
        if (!checkPW(oldPW)) {
            return false;
        }
        try {
            password = Encrypt.encrypt(newPW);
            Configuration.getPlugin().getManager().setPassword(name, password);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(AMPlayer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (AlgorithmException ex) {
            Logger.getLogger(AMPlayer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * This checks to see if the player has created a password. This is done by
     * checking to see if the password is equal to "None".
     *
     * @return True if the player's password is not "None", otherwise false
     */
    public boolean registered() {
        try {
            return !(Encrypt.areEqual(password, Encrypt.encrypt("None")));
        } catch (AlgorithmException ex) {
            return true;
        }
    }

    public void setLoginLocation(Location location) {
        loginLoc = location;
    }
}