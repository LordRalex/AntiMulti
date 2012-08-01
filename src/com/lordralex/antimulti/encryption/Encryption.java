/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.encryption;

/**
 *
 * @author Joshua
 */
public enum Encryption {

    MD5("md5"),
    WHIRLPOOL("whirlpool");
    private String name;

    private Encryption(String aName) {
        name = aName;
    }

    public String getName() {
        return name;
    }

    public static Encryption getEncryption(String test) {
        Encryption[] enc = Encryption.values();
        for (Encryption e : enc) {
            if (e.getName().equalsIgnoreCase(test)) {
                return e;
            }
        }
        return Encryption.MD5;
    }
}
