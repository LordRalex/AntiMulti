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
