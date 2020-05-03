package drama.painter.core.web.asserts;

import drama.painter.core.web.enums.BaseEnum;
import drama.painter.core.web.misc.Result;

/**
 * @author murphy
 */
public class BaseException extends RuntimeException implements BaseEnum {
    static final int GLOBAL_ERROR_CODE = -999999;
    final int value;
    final Object[] args;

    public BaseException(int value, String msg) {
        this(value, msg, null);
    }

    public BaseException(int value, String msg, Object[] args) {
        super(msg);
        this.value = value;
        this.args = args;
        if (value == GLOBAL_ERROR_CODE) {
            throw new RuntimeException("自定义错误代号不能为" + GLOBAL_ERROR_CODE);
        }
    }

    public BaseException(Throwable e, Object... args) {
        super(e);
        this.value = Result.FAIL.getCode();
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return this.getMessage();
    }
}
