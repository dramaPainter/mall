package drama.painter.core.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
public enum StatusEnum implements BaseEnum {
    /**
     * 冻结
     */
    DISABLE(0, "冻结"),
    /**
     * 启用
     */
    ENABLE(1, "启用");

    static final List<StatusEnum> MAP = Arrays.asList(StatusEnum.values());
    final int value;
    final String name;

    StatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static StatusEnum apply(int code) {
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
