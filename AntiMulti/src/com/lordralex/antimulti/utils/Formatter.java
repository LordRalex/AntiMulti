package com.lordralex.antimulti.utils;

import java.util.regex.Pattern;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This contains the extra formatting utilities that the rest of the plugin may
 * used. This does not use any of the AntiMulti's features so this can be used
 * in any class without needing to hook into anything else
 *
 * @version 1.1
 * @author Lord_Ralex
 * @since 1.2
 */
public final class Formatter {

    private static Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-F])");
    private static Pattern chatMagicPattern = Pattern.compile("(?i)&([K])");
    private static Pattern chatBoldPattern = Pattern.compile("(?i)&([L])");
    private static Pattern chatStrikethroughPattern = Pattern.compile("(?i)&([M])");
    private static Pattern chatUnderlinePattern = Pattern.compile("(?i)&([N])");
    private static Pattern chatItalicPattern = Pattern.compile("(?i)&([O])");
    private static Pattern chatResetPattern = Pattern.compile("(?i)&([R])");

    /**
     * This handles the color codes and formatting related codes.
     *
     * @param original The message to handle the color codes for
     * @return The modified message with codes replaced
     */
    public static String handleColorCodes(String original) {
        if (original == null) {
            return "";
        }

        String newstring = original;
        newstring = chatColorPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatMagicPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatBoldPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatStrikethroughPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatUnderlinePattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatItalicPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatResetPattern.matcher(newstring).replaceAll("\u00A7$1");
        return newstring;
    }

    /**
     * This removes the color codes and formatting related codes and returns the
     * color-less/format-less message.
     *
     * @param original The message to remove the color and formatting from
     * @return The modified message with codes removed
     */
    public static String stripColors(String original) {
        if (original == null) {
            return "";
        }
        String newString = original;
        newString = ChatColor.stripColor(newString);
        return newString;
    }

    /**
     * This will replace {Player} with the player's name. This is to be used for
     * ease but can be used instead of string.replace("{Player}", name)
     *
     * @param message The message to replace
     * @param playerName The player's name
     * @return The new message with the {Player} replaced
     */
    public static String replacePlayerHolder(String message, String playerName) {
        return replaceTag(message, "Player", playerName);
    }

    /**
     * This will replace {Player} with the player's name. This is to be used for
     * ease but can be used instead of string.replace("{Player}",
     * player.getName())
     *
     * @param message The message to replace
     * @param player The player object
     * @return The new message with the {Player} replaced
     */
    public static String replacePlayerHolder(String message, Player player) {
        return replacePlayerHolder(message, player.getName());
    }

    /**
     * This is a general tag replacer. This will replace any tag with a variable
     * for easier replacement.
     *
     * @param message The message to replace
     * @param tag The tag to replace, which is formatted as "{tag}"
     * @param var The variable to replace the tag with
     * @return The message with the tag replaced
     */
    public static String replaceTag(String message, String tag, String var) {
        String newMessage = message;
        newMessage = newMessage.replace("{" + tag + "}", var);
        return newMessage;
    }

    /**
     * This gets the formatted time from the milliseconds. This returns in the
     * format "dd-MM-yyyy HH:mm:ss"
     *
     * @param time The milliseconds to get the time from
     * @return The time in the format "dd-MM-yyyy HH:mm:ss"
     */
    public static String parseTime(long time) {
        return DateFormatUtils.format(time, "dd-MM-yyyy HH:mm:ss");
    }

    /**
     * This gets the formatted time from the current time. This returns in the
     * format "dd-MM-yyyy HH:mm:ss". This uses the System.currentTimeMillis()
     *
     * @return The time in the format "dd-MM-yyyy HH:mm:ss"
     */
    public static String getTime() {
        return parseTime(System.currentTimeMillis());
    }

    private Formatter() {
    }
}
