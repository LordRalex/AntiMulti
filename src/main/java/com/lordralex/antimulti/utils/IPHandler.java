package com.lordralex.antimulti.utils;

import com.lordralex.antimulti.logger.AMLogger;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * Handles comparison of 2 IPs. This will allow checks using wildcards, but not
 * ranges.
 *
 * @version 3.0
 * @author Lord_Ralex
 * @since 1.2
 */
public final class IPHandler {

    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*),
     * so for example, 192.168.1.4 will return true with 192.168.1.*, but not
     * with 192.168.1.1-10
     *
     * @param ip1 The first IP to check
     * @param ip2 The second IP to check
     * @return True if the ips are the same, false otherwise
     * @since 1.0
     */
    public static boolean contains(String ip1, String ip2) {
        try {
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
                if (!partA.equalsIgnoreCase("*") && !partB.equalsIgnoreCase("*")) {
                    int one = Integer.parseInt(partA);
                    int two = Integer.parseInt(partB);
                    if (one != two) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            AMLogger.error(e);
            return false;
        }
    }

    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*),
     * so for example, 192.168.1.4 will return true with 192.168.1.*, but not
     * with 192.168.1.1-10
     *
     * @param ip The first IP to check
     * @param ip2 The second IP to check
     * @return True if the IPs are the same, false otherwise
     * @see contains(String, String)
     * @since 1.0
     */
    public static boolean contains(InetAddress ip, InetAddress ip2) {
        return contains(ip.getHostAddress(), ip2.getHostAddress());
    }
    
    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*),
     * so for example, 192.168.1.4 will return true with 192.168.1.*, but not
     * with 192.168.1.1-10
     *
     * @param ip The first IP to check
     * @param ip2 The second IP to check
     * @return True if the IPs are the same, false otherwise
     * @see contains(String, String)
     * @since 3.0
     */
    public static boolean contains(String ip, InetAddress ip2) {
        return contains(ip, ip2.getHostAddress());
    }
    
    /**
     * Checks 2 IPs to see if they match. This also checks for the wildcard (*),
     * so for example, 192.168.1.4 will return true with 192.168.1.*, but not
     * with 192.168.1.1-10
     *
     * @param ip The first IP to check
     * @param ip2 The second IP to check
     * @return True if the IPs are the same, false otherwise
     * @see contains(String, String)
     * @since 3.0
     */
    public static boolean contains(InetAddress ip, String ip2) {
        return contains(ip.getHostAddress(), ip2);
    }

    /**
     * Checks a list to see if an ip is listed. This also checks for the
     * wildcard (*), so for example, 192.168.1.4 will return true with
     * 192.168.1.*, but not with 192.168.1.1-10
     *
     * @param list The list of IPs to check with
     * @param ip The ip to attempt to find
     * @return True if the list contains the IP, false otherwise
     * @since 1.0
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
