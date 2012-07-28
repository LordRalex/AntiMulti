package com.lordralex.antimulti.files;

/**
 * @version 1.0
 * @author Joshua
 */
public abstract class Manager {

    public abstract String getPassword(String name);

    public abstract String setPassword(String pass, String newPass);

    public abstract String setup();
}
