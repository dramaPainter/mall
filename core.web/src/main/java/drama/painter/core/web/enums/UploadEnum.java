package drama.painter.core.web.enums;

/**
 * @author murphy
 */
public enum UploadEnum implements BaseEnum {
    /**
     * ordinal是以0开始
     */
    NONE(0, ""),

    /**
     * 员工头像
     */
    HEAD(1, "/upload/head/{date}/{uuid}.jpg"),

    /**
     * 商品图列表
     */
    PRODUCT(2, "/upload/product/{date}/{uuid}.jpg");

    final int value;
    final String name;

    UploadEnum(int value, String name) {
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

