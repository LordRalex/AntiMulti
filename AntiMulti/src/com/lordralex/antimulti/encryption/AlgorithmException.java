package com.lordralex.antimulti.encryption;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.2
 */
public class AlgorithmException extends Exception {

    /**
     * Creates a new instance of
     * <code>AlgorithmException</code> without detail message.
     */
    public AlgorithmException() {
        super("An error occured during the encryption process");
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
