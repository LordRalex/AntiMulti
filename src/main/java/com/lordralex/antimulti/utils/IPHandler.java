package com.lordralex.antimulti.utils;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Handles comparison of 2 IPs. This allows checks of IPs including ranges and
 * wildcards.
 *
 * @version 3.0.0
 * @author Lord_Ralex
 */
public final class IPHandler {

    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*)
     * and ranges (x-y)
     *
     * @param ip1 The first IP to check
     * @param ip2 The second IP to check
     * @return True if the ips are the same, false otherwise
     */
    public static boolean contains(String ip1, String ip2) {
        if (ip1.equalsIgnoreCase(ip2)) {
            return true;
        }
        String[] ip1parts = ip1.split(".");
        if (ip1parts.length != 4) {
            String[] temp = new String[4];
            Arrays.fill(temp, "*");
            System.arraycopy(ip1parts, 0, temp, 0, ip1parts.length);
            ip1parts = temp;
        }
        String[] ip2parts = ip1.split(".");
        if (ip2parts.length != 4) {
            String[] temp = new String[4];
            Arrays.fill(temp, "*");
            System.arraycopy(ip2parts, 0, temp, 0, ip2parts.length);
            ip2parts = temp;
        }
        for (int i = 0; i < 4; i++) {
            String partA = ip1parts[i];
            String partB = ip2parts[i];
            if (partA.contains("-")) {
                try {
                    int num1 = Integer.parseInt(partA.split("-")[0]);
                    int num2 = Integer.parseInt(partA.split("-")[1]);
                    int test = Integer.parseInt(partB);
                    if (num1 <= test && test <= num2) {
                    }
                } catch (NumberFormatException e) {
                    return false;
                } catch (IndexOutOfBoundsException e) {
                    return false;
                }
            }
            if (!partA.equalsIgnoreCase("*") && !partB.equalsIgnoreCase("*")) {
                try {
                    int one = Integer.parseInt(partA);
                    int two = Integer.parseInt(partB);
                    if (one != two) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*)
     * and ranges (x-y)
     *
     * @param ip The first IP to check
     * @param ip2 The second IP to check
     * @return True if the IPs are the same, false otherwise
     * @see contains(String, String)
     */
    public static boolean contains(InetAddress ip, InetAddress ip2) {
        return contains(ip.getHostAddress(), ip2.getHostAddress());
    }

    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*)
     * and ranges (x-y)
     *
     * @param ip The first IP to check
     * @param ip2 The second IP to check
     * @return True if the IPs are the same, false otherwise
     * @see contains(String, String)
     */
    public static boolean contains(String ip, InetAddress ip2) {
        return contains(ip, ip2.getHostAddress());
    }

    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*)
     * and ranges (x-y)
     *
     * @param ip The first IP to check
     * @param ip2 The second IP to check
     * @return True if the IPs are the same, false otherwise
     * @see contains(String, String)
     */
    public static boolean contains(InetAddress ip, String ip2) {
        return contains(ip.getHostAddress(), ip2);
    }

    /**
     * Checks a list of IPs to see if one is in it. This also checks for the
     * wildcard (*) and ranges (x-y)
     *
     * @param list The list of IPs to check with
     * @param ip The ip to attempt to find
     * @return True if the list contains the IP, false otherwise
     */
    public static boolean contains(List<String> list, String ip) {
        for (String test : list) {
            if (contains(test, ip)) {
                return true;
            }
        }
        return false;
    }

    private IPHandler() {
    }
}
