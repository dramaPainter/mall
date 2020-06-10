package drama.painter.core.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author murphy
 */
public enum YesNoEnum implements BaseEnum {
    /**
     * 否
     */
    NO(0, "否"),
    /**
     * 是
     */
    YES(1, "是");

    final int value;
    final String name;

    YesNoEnum(int value, String name) {
        this.value = value;
        this.name = name;
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
