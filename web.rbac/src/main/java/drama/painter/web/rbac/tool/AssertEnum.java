package drama.painter.web.rbac.tool;

import drama.painter.core.web.asserts.BaseAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author murphy
 */
@Getter
@AllArgsConstructor
public enum AssertEnum implements BaseAssert {
    /**
     * 对象不存在
     */
    NOT_FOUND(-101, "[%s]不存在");

    private final int value;
    private final String name;
}
