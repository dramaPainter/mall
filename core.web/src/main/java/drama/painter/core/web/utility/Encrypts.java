package drama.painter.core.web.utility;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author murphy
 */
public class Encrypts {
    static final int TWO = 2;
    static final MessageDigest DIGEST;
    static BigInteger m_key;

    static {
        try {
            DIGEST = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        m_key = new BigInteger("0");
        String config = "201805010099999";
        char[] key = (config + "\0").toCharArray();
        int len = key.length;
        for (int i = 0; i < len; i++) {
            BigInteger big = new BigInteger("2").pow(8 * (i % 8)).multiply(new BigInteger(String.valueOf((byte) key[i])));
            m_key = m_key.add(big);
        }
    }

    public static byte[] encryptByte(String text) {
        byte[] source = text.getBytes();
        return coreEncrypt(source, false);
    }

    public static String encrypt(String text) {
        byte[] dest = encryptByte(text);
        return Strings.toByteString(dest);
    }

    public static String decrypt(byte[] source) {
        try {
            byte[] dest = coreEncrypt(source, true);
            return new String(dest);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String text) {
        try {
            if (StringUtils.isEmpty(text) || text.length() % TWO == 1) {
                return "";
            }
            byte[] source = new byte[text.length() / TWO];
            for (int i = 0; i < text.length() / TWO; ++i) {
                source[i] = (byte) (Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16) & 0xff);
            }
            return decrypt(source);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] coreEncrypt(byte[] source, boolean isDest) {
        int len = source.length;
        byte[] dest = new byte[len];

        byte mask = m_key.and(new BigInteger("255")).byteValue();
        dest[0] = (byte) (source[0] ^ mask);
        for (int i = 1; i < len; i++) {
            dest[i] = (byte) (source[i] ^ mask);
            mask = isDest ? dest[mask % i] : source[mask % i];
        }
        return dest;
    }

    public static String md5(String text) {
        byte[] byteArray;
        try {
            byteArray = text.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
        return Strings.toByteString(DIGEST.digest(byteArray));
    }

    @SneakyThrows
    public static String urlEncode(String text) {
        return URLEncoder.encode(text, "utf-8");
    }

    @SneakyThrows
    public static String urlDecode(String text) {
        return URLDecoder.decode(text, "utf-8");
    }
}
