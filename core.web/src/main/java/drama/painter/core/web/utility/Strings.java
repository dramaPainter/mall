package drama.painter.core.web.utility;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author murphy
 */
public class Strings {
    private static final Object LOCK = new Object[0];
    private static int INCREMENT = 0;

    public static String reset(String val) {
        return StringUtils.isEmpty(val) ? "" : val.trim();
    }

    public static long getOrderId(String prefix) {
        int value;
        synchronized (LOCK) {
            INCREMENT = INCREMENT == 999 ? 0 : ++INCREMENT;
            value = INCREMENT;
        }
        String time = Dates.toDateTimeNumber();
        String suffix = padLeft(String.valueOf(value), 3, '0');
        return Long.parseLong(prefix.concat(time).concat(suffix));
    }

    public static String padLeft(String src, int length, char letter) {
        int diff = length - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[length];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < length; i++) {
            charr[i] = letter;
        }
        return new String(charr);
    }

    public static String toByteString(byte[] dest) {
        StringBuilder buf = new StringBuilder(dest.length * 2);
        for (byte b : dest) {
            buf.append(String.format("%02x", b & 0xff));
        }
        return buf.toString();
    }

    public static byte[] fromByteString(String src) {
        byte[] dest = new byte[src.length() / 2];
        for (int i = 0; i < dest.length; i++) {
            dest[i] = (byte) Integer.parseInt(src.substring(i * 2, i * 2 + 2), 16);
        }
        return dest;
    }

    @SneakyThrows
    public static String urlencode(String text) {
        return URLEncoder.encode(text, "utf-8");
    }

    @SneakyThrows
    public static String urldecode(String text) {
        return URLDecoder.decode(text, "utf-8");
    }
}
