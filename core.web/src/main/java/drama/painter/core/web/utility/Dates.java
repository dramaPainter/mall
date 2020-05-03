package drama.painter.core.web.utility;

import drama.painter.core.web.validator.DateValidator;
import drama.painter.core.web.validator.Validator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author murphy
 */
public class Dates {
    static final Validator SCAN = new DateValidator();
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final DateTimeFormatter DATETIME_MILLIS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    static final DateTimeFormatter DATE_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    static final DateTimeFormatter DATETIME_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
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
        if (SCAN.validate(date)) {
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
