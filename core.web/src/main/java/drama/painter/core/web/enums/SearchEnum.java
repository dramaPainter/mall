package drama.painter.core.web.enums;

/**
 * @author murphy
 */
public enum SearchEnum implements BaseEnum {
    /**
     * 帐号ID
     */
    ID(1, "ID"),
    /**
     * 按名称查询
     */
    NAME(2, "名称"),
    /**
     * 按昵称查询
     */
    ALIAS(3, "昵称"),
    /**
     * 按URL查询
     */
    URL(4, "URL");

    final int value;
    final String name;

    SearchEnum(int value, String name) {
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

