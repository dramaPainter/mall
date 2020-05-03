package drama.painter.core.web.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class Dates {
    static final Pattern PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}(\\s\\d{2}:\\d{2}:\\d{2})?(,\\d{3})?$");
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final DateTimeFormatter DATETIME_MILLIS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    static final DateTimeFormatter DATE_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    @SuppressWarnings("SpellCheckingInspection")
    static final DateTimeFormatter DATETIME_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @SuppressWarnings("SpellCheckingInspection")
    static final DateTimeFormatter DATETIME_NUMBER_MILLIS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    static final ZoneId CHINA = ZoneId.of("Asia/Shanghai");
    static final ZoneOffset OFFSET = ZoneOffset.ofHours(8);

    public static String toDate() {
        return DATE_FORMAT.format(LocalDateTime.now(CHINA));
    }

    public static String toDateTime() {
        return DATETIME_FORMAT.format(LocalDateTime.now(CHINA));
    }

    public static String toDateTimeMillis() {
        return DATETIME_MILLIS_FORMAT.format(LocalDateTime.now(CHINA));
    }

    public static String toDateNumber() {
        return DATE_NUMBER_FORMAT.format(LocalDateTime.now(CHINA));
    }

    public static String toDateTimeNumber() {
        return DATETIME_NUMBER_FORMAT.format(LocalDateTime.now(CHINA));
    }

    public static String toDateTimeMillisNumber() {
        return DATETIME_NUMBER_MILLIS_FORMAT.format(LocalDateTime.now(CHINA));
    }

    public static String modify(String date, int minute, DateTimeType returnType, String defaultValue) {
        if (PATTERN.matcher(date).matches()) {
            DateTimeFormatter formatter = date.contains(",") ? DATETIME_MILLIS_FORMAT : (date.contains(":") ? DATETIME_FORMAT : DATE_FORMAT);
            LocalDateTime time = LocalDateTime.parse(date, formatter).plusMinutes(minute);
            switch (returnType) {
                case DATE:
                    return time.format(DATE_FORMAT);
                case DATE_TIME:
                    return time.format(DATETIME_FORMAT);
                case DATE_TIME_MILLIS:
                    return time.format(DATETIME_MILLIS_FORMAT);
                default:
                    throw new RuntimeException("没有可用的日期格式化类型");
            }
        } else {
            return defaultValue;
        }
    }

    public enum DateTimeType {
        DATE, DATE_TIME, DATE_TIME_MILLIS
    }
}
