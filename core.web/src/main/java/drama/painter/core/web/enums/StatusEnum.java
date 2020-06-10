package drama.painter.core.web.enums;

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

    final int value;
    final String name;

    StatusEnum(int value, String name) {
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
