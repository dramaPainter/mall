package drama.painter.core.web.asserts;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public interface BaseAssert extends IAssert, BaseEnum {
    /**
     * 新建一个自定义的Exception
     *
     * @param args 参数
     * @return
     */
    @Override
    default BaseException newException(Object... args) {
        String msg = args == null ? this.getName() : String.format(this.getName(), args);
        return new BaseException(this.getValue(), msg);
    }
}
