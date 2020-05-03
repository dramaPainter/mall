package drama.painter.core.web.ftp.upload;

import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.utility.Randoms;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 */
class UploadUrl {
    private static final Map<String, ArgFunction<Integer, Long, Integer, String>> MAP = new HashMap();

    static {
        MAP.put("{i}", (userid, id, i) -> String.valueOf(i + 1));
        MAP.put("{userid}", (userid, id, i) -> String.valueOf(userid));
        MAP.put("{userid%10}", (userid, id, i) -> String.valueOf(userid % 10));
        MAP.put("{userid%100}", (userid, id, i) -> String.valueOf(userid % 100));
        MAP.put("{id}", (userid, id, i) -> String.valueOf(id));
        MAP.put("{id%100}", (userid, id, i) -> String.valueOf(id % 100));
        MAP.put("{yyyyMM}", (userid, id, i) -> Dates.toDate().substring(0, 7).replaceFirst(",", ""));
        MAP.put("{yyyyMMdd}", (userid, id, i) -> Dates.toDate());
        MAP.put("{yyyyMMddHHmmss}", (userid, id, i) -> Dates.toDateTimeNumber());
        MAP.put("{uuid}", (userid, id, i) -> Randoms.getNonceString());
    }

    public static String format(String text, int userid, long id, int i) {
        for (Map.Entry<String, ArgFunction<Integer, Long, Integer, String>> entry : MAP.entrySet()) {
            if (text.contains(entry.getKey())) {
                text = text.replace(entry.getKey(), entry.getValue().apply(userid, id, i));
            }
        }
        return text;
    }


    @FunctionalInterface
    private interface ArgFunction<T, U, V, R> {
        /**
         * 多参数函数接口
         *
         * @param t
         * @param u
         * @param v
         * @return
         */
        R apply(T t, U u, V v);
    }
}
