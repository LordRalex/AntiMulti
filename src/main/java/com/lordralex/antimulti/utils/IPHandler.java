package com.lordralex.antimulti.utils;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public final class IPHandler {

    public static boolean contains(String ip1, String ip2) {
        if (ip1.equalsIgnoreCase(ip2)) {
            return true;

        }
        /*
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

         }*/
        return false;
    }

    public static boolean contains(InetAddress ip, InetAddress ip2) {
        return contains(ip.getHostAddress(), ip2.getHostAddress());
    }

    public static boolean contains(String ip, InetAddress ip2) {
        return contains(ip, ip2.getHostAddress());
    }

    public static boolean contains(InetAddress ip, String ip2) {
        return contains(ip.getHostAddress(), ip2);
    }

    public static boolean contains(List<String> list, String ip) {
        for (String test: list) {
            if (contains(test, ip)) {
                return true;
            }
        }
        return false;
    }

    private IPHandler() {
    }
}
