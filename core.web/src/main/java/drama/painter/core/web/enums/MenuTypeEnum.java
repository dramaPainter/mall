package drama.painter.core.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
public enum MenuTypeEnum implements BaseEnum {
    /**
     * 子项
     */
    ITEM(0, "子项"),
    /**
     * 页面
     */
    PAGE(1, "页面"),
    /**
     * 菜单
     */
    MENU(2, "菜单");

    static final List<MenuTypeEnum> MAP = Arrays.asList(MenuTypeEnum.values());
    final int value;
    final String name;

    MenuTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static MenuTypeEnum apply(int code) {
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
