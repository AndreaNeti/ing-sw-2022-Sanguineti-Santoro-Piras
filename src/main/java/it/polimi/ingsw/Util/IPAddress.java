package it.polimi.ingsw.Util;

public class IPAddress {
    // return null if it's not a valid ip, otherwise returns the IP bytes
    public static byte[] getIpFromString(String ip) {
        ip = ip.toLowerCase();
        if (ip.equals("localhost") || ip.equals("l") || ip.equals("paolino")) return new byte[]{127, 0, 0, 1};
        String[] bytes = ip.split("[.]");
        if (bytes.length != 4) return null;
        byte[] ret = new byte[4];
        for (byte i = 0; i < 4; i++) {
            int x; // just because java doesn't have unsigned bytes
            try {
                x = Integer.parseInt(bytes[i]);
            } catch (NumberFormatException e) {
                return null;
            }
            if (x < 0 || x > 255) return null;
            ret[i] = (byte) x;
        }
        return ret;
    }
}
