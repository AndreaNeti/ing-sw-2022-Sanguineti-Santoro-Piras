package it.polimi.ingsw.utils;

/**
 * IPAddress class is used to convert the string of the IP address inputted by the user to connect
 * to a server into an array of bytes. <br>
 */
public class IPAddress {
    /**
     * Method getIpFromString converts the string of the IP address into an equivalent array of bytes.
     *
     * @param ip of type {@code String} - string of the address inputted by the user.
     * @return {@code byte[]} - equivalent array of bytes of the address if the string is valid, null otherwise.
     */
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
