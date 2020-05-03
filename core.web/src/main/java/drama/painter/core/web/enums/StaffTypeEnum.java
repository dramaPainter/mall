package drama.painter.core.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
public enum StaffTypeEnum implements BaseEnum {
    /**
     * 所有类型
     */
    ALL(0, "所有类型"),
    /**
     * 其它客服
     */
    CS_OTHER(1, "其他客服帐号"),
    /**
     * 充值客服
     */
    CS_RECHARGE(2, "充值客服帐号"),
    /**
     * 兑换客服
     */
    CS_EXCHANGE(4, "兑换客服帐号"),
    /**
     * 客服组长
     */
    CS_MANAGER(8, "客服组长帐号"),
    /**
     * 单平台帐号
     */
    PLATFORM_ONE(16, "单平台帐号"),
    /**
     * 全平台帐号（限制员工帐号管理和钱有关的操作）
     */
    PLATFORM_ALL(32, "全平台帐号"),
    /**
     * 内部帐号（限制员工帐号管理操作）
     */
    PLATFORM_NATIVE(64, "内部帐号"),
    /**
     * 管理员帐号（不限操作）
     */
    PLATFORM_ADMIN(128, "管理员帐号");

    static final List<StaffTypeEnum> MAP = Arrays.asList(StaffTypeEnum.values());
    final int value;
    final String name;

    StaffTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static StaffTypeEnum apply(int code) {
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
