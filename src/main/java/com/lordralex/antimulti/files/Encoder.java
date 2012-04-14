/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.files;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Joshua
 */
public class Encoder {
    
    public static String encode(String message) throws NoSuchAlgorithmException
    {
        MessageDigest mdEnc = MessageDigest.getInstance("MD5");
        mdEnc.update(message.getBytes(), 0, message.length());
        String encoded = new BigInteger(1, mdEnc.digest()).toString(16);
        return encoded;
    }
    
    public static boolean areEqual(String message1, String message2) throws NoSuchAlgorithmException
    {
        if(message1.equals(message2))
            return true;
        String temp1 = encode(message1);
        if(temp1.equals(message2))
            return true;
        String temp2 = encode(message2);
        if(message1.equals(temp2))
            return false;
        if(temp1.equals(temp2))
            return false;
        return false;
    }
    
}
