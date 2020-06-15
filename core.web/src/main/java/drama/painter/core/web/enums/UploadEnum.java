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
     * 首页轮播图
     */
    INDEX_CAROUSEL(1, "/upload/index/{uuid}.jpg"),

    /**
     * 商品轮播图
     */
    PRODUCT_CAROUSEL(2, "/upload/product/{date}/{uuid}.jpg"),

    /**
     * 员工头像
     */
    STAFF_HEAD(3, "/upload/head/{date}/{uuid}.jpg");

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

