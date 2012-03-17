/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.antimulti.mySQL;

/**
 *
 * @author Joshua
 */
public class SQLDataException extends Exception {

    /**
     * Creates a new instance of
     * <code>SQLDataException</code> with default message.
     */
    public SQLDataException() {
        super("SQL Data setup error");
    }

    /**
     * Constructs an instance of
     * <code>SQLDataException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SQLDataException(String msg) {
        super(msg);
    }
}
