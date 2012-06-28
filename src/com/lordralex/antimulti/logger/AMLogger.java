package com.lordralex.antimulti.logger;

import com.lordralex.antimulti.AntiMulti;
import com.lordralex.antimulti.utils.Formatter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;

/**
 * This is the AntiMulti logger. This should be used since this is able to write
 * to a file and to the console.
 *
 * @version 1.0
 * @author Joshua
 * @since 1.2
 */
public class AMLogger {

    private static final Logger logger = Bukkit.getLogger();
    private static BufferedWriter writer = null;

    /**
     * Creates the writer and logger instance for the rest of the class. MUST BE
     * USED BEFORE WRITING
     *
     * @param plugin The plugin
     */
    public static void setup(AntiMulti plugin) {
        try {
            writer = new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), "logs" + File.separator + DateFormatUtils.format(System.currentTimeMillis(), "dd-MM-yyyy hh-mm-ss") + ".log")));
            writer = null;
        } catch (IOException e) {
            logger.severe("Error occured while setting up AM logger, will not log to file");
            writer = null;
        }
    }

    /**
     * Writes a message to the console and file with the "INFO" priority
     *
     * @param message The message to write
     */
    public static void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * Writes a message to the console and file with the "WARNING" priority
     *
     * @param message The message to write
     */
    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    /**
     * Writes a message to the console and file with the "SEVERE" priority
     *
     * @param message The message to write
     */
    public static void severe(String message) {
        log(Level.SEVERE, message);
    }

    /**
     * Writes a message to the console and file with any priority passed
     *
     * @param level The Level to log as
     * @param message The message to write
     */
    public static void log(Level level, String message) {
        logger.log(level, message);
        writeToFile(level.toString() + ": " + message);
    }

    /**
     * Writes a line to the file with the current time.
     *
     * @param message The message to write to the file
     */
    public static void writeToFile(String message) {
        writeToFile(message, System.currentTimeMillis());
    }

    /**
     * Writes a line to the file with the time passed in the parameter. This
     * should not be called unless the System.currentTimeMillis() is used as the
     * long parameter.
     *
     * @param message The message to write to the file
     * @param time The time in milliseconds to log the time as
     */
    public static void writeToFile(String message, long time) {
        writeToFile(message, Formatter.parseTime(time));
    }

    /**
     * Writes a line to the file with the time.
     *
     * @param message The message to write to the file
     * @param time The time to log as. Usually formatted as dd-MM-yyyy HH:mm:ss
     */
    public static void writeToFile(String message, String time) {
        if (writer != null) {
            try {
                writer.write(time + "- " + message);
                writer.newLine();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error writing to file:", ex);
            }
        }
    }

    /**
     * Writes an error to the file. This should be used after an error is thrown
     * so that it can be logged to the file for future reference.
     *
     * @param error The error that was thrown
     * @param message A message to add to the error if desired. Can be null.
     */
    public static void writeToFile(Throwable error, String message) {
        if (writer != null) {
            if (message != null && !message.isEmpty()) {
                writeToFile("ERROR MESSAGE: " + message);
            }
            writeToFile("ERROR: " + error.getLocalizedMessage());
            StackTraceElement[] stack = error.getStackTrace();
            for (StackTraceElement stackTrace : stack) {
                writeToFile(stackTrace.toString());
            }
        }
    }

    /**
     * Logs an error to the console. Can be used instead of
     * AMLogger.writeToFile(error, null)
     *
     * @param error The error thrown
     */
    public static void writeToFile(Throwable error) {
        error(error, null);
    }

    /**
     * Closes the logger and writers. This should only be used when the plugin
     * is disabling.
     */
    public static void close() {
        if (writer != null) {
            try {
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "ERROR CLOSING LOGGERS", ex);
            }
        }
    }

    /**
     * Logs an error to the console and to the file.
     *
     * @param error The error thrown
     * @param message Message to add to error if desired. Can be null.
     */
    public static void error(Throwable error, String message) {
        logger.log(Level.SEVERE, message, error);
        writeToFile(error, message);
    }

    /**
     * Logs an error to the file and console. Can be used instead of
     * AMLogger.error(error, null)
     *
     * @param error The error thrown
     */
    public static void error(Throwable error) {
        error(error, null);
    }
}
