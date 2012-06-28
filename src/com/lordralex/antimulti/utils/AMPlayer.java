package com.lordralex.antimulti.utils;

import com.lordralex.antimulti.config.Configuration;
import com.lordralex.antimulti.encryption.AlgorithmException;
import com.lordralex.antimulti.encryption.Encrypt;
import com.lordralex.antimulti.logger.AMLogger;
import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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

    private String name;
    private String password;
    private boolean loggedIn;
    private Location loginLoc;

    /**
     * Creates a new AMPlayer using the data sent using the player object.
     *
     * @param player
     */
    public AMPlayer(Player player) {
        name = player.getName();
        loginLoc = player.getLocation();
        File passwordFile = new File(Configuration.getPlugin().getUserFolder(), "passwords");
        if (!passwordFile.exists()) {
            passwordFile.mkdirs();
        }
        passwordFile = new File(passwordFile, name + ".yml");
        FileConfiguration passwordConfig = YamlConfiguration.loadConfiguration(passwordFile);
        try {
            password = passwordConfig.getString("password", Encrypt.encrypt("None"));
            passwordConfig.set("password", password);
            passwordConfig.save(passwordFile);
        } catch (AlgorithmException e) {
            AMLogger.error(e);
            password = "3arubvgemfch9t99l4m43gudqk";
        } catch (IOException e) {
            AMLogger.error(e);
        }
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
     * Sets the location of the login. Used to patch the login/join issue where
     * players are defined to be located at spawn during Login when the AMPlayer
     * is defined. This is the patch to that issue
     *
     * @param loc The new location to set
     */
    public void setLoginLocation(Location loc) {
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
        String temp = password;
        try {
            password = Encrypt.encrypt(newPW);
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(
                    new File(Configuration.getPlugin().getUserFolder(), "passwords" + File.separator + name + ".yml"));
            playerData.set("password", password);
            playerData.save(new File(Configuration.getPlugin().getUserFolder(), "passwords" + File.separator + name + ".yml"));
            return true;
        } catch (IOException ex) {
            AMLogger.error(ex);
            password = temp;
            return false;
        } catch (AlgorithmException ex) {
            AMLogger.error(ex);
            password = temp;
            return false;
        } catch (Exception ex) {
            AMLogger.error(ex);
            password = temp;
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
}