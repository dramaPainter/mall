package drama.painter.core.web.log.fmt;

import com.fasterxml.jackson.annotation.JsonCreator;
import drama.painter.core.web.enums.BaseEnum;

import java.util.Arrays;
import java.util.List;


/**
 * @author murphy
 */
public enum LogFormat implements BaseEnum {
    /**
     * 通用系统日志
     */
    API(1, "通用系统日志"),
    /**
     * 网站日志
     */
    HTTP(2, "网站日志"),
    /**
     * SQL日志
     */
    SQL(3, "SQL日志");

    static final List<LogFormat> MAP = Arrays.asList(LogFormat.values());
    final int value;
    final String name;

    LogFormat(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static LogFormat apply(int code) {
        return MAP.stream().filter(o -> o.getValue() == code).findAny().orElse(null);
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
