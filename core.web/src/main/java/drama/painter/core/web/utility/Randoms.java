package drama.painter.core.web.utility;

import java.util.Random;
import java.util.UUID;

/**
 * @author murphy
 */
public class Randoms {
    static final Random RANDOM = new Random();
    static final char[] CHARS = new char[]{
            '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',
            'k', 'm', 'n', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    public static String getRandom(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int s = RANDOM.nextInt(CHARS.length);
            sb.append(CHARS[s]);
        }
        return sb.toString();
    }

    public static String getRandomNumber(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int s = RANDOM.nextInt(8);
            sb.append(CHARS[s]);
        }
        return sb.toString();
    }

    public static int nextInt(int value) {
        return RANDOM.nextInt(value);
    }

    public static String getNonceString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
