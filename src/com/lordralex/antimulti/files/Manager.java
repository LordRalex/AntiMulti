package com.lordralex.antimulti.files;

import java.io.IOException;

/**
 * @version 1.0
 * @since 2.0.5
 */
public interface Manager {

    /**
     * Gets the password for a player. This will get the encrypted version so
     * may not be the same if the encryption in use is different.
     *
     * @param name The name of the player
     * @return The password as stored in the system. This can be in any
     * encryption.
     */
    public abstract String getPassword(String name);

    /**
     * Sets the password of a player to something different. This does not check
     * the values, just writes it.
     *
     * @param name Name of player
     * @param newPass New password
     * @throws IOException
     */
    public abstract void setPassword(String name, String newPass) throws IOException;

    /**
     * Returns the IPs that have been used by the player.
     *
     * @param name The name of the player
     * @return The array of IPs that has been used
     */
    public abstract String[] getIPs(String name);

    /**
     * Returns the list of names that have used a particular ip.
     *
     * @param ip The ip to check
     * @return The array of names used on that IP
     */
    public abstract String[] getNames(String ip);

    /**
     * Adds an IP to a name.
     *
     * @param name Name of the player
     * @param ip IP to add to the name
     */
    public abstract void addIP(String name, String ip);

    /**
     *
     * @param ip IP to edit
     * @param name Name to add to the IP
     */
    public abstract void addName(String ip, String name);

    /**
     * Sets up the manager. This should be used to set up the manager, and
     * return itself or another manager if setup fails.
     *
     * @return
     */
    public abstract Manager setup();

    /**
     * Closes the manager.
     */
    public abstract void close();
}