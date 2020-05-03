package drama.painter.core.web.utility;

/**
 * @author murphy
 */
public class Ip {
    public static long toLong(String ip) {
        long result = 0;
        String[] d = ip.split("\\.");
        for (String b : d) {
            result <<= 8;
            result |= Long.parseLong(b) & 0xff;
        }
        return result;
    }
}
