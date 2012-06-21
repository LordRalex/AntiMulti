/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.encryption;

/**
 *
 * @author Joshua
 */
public class AlgorithmException extends Exception {

    /**
     * Creates a new instance of
     * <code>AlgorithmException</code> without detail message.
     */
    public AlgorithmException() {
        super("An error occured during the encrption process");
    }

    /**
     * Constructs an instance of
     * <code>AlgorithmException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AlgorithmException(String msg) {
        super(msg);
    }
}
