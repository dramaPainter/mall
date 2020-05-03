package drama.painter.core.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
public enum PlatformEnum implements BaseEnum {
    /**
     * 全平台
     */
    ALL(0, "全平台"),
    /**
     * 默认
     */
    PLATFORM_1(1, "默认"),
    /**
     * 2平台
     */
    PLATFORM_2(2, "2平台"),
    /**
     * 3平台
     */
    PLATFORM_3(3, "3平台"),
    /**
     * 4平台
     */
    PLATFORM_4(4, "4平台"),
    /**
     * 5平台
     */
    PLATFORM_5(5, "5平台");

    static final List<PlatformEnum> MAP = Arrays.asList(PlatformEnum.values());
    final int value;
    final String name;

    PlatformEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static PlatformEnum apply(int code) {
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
