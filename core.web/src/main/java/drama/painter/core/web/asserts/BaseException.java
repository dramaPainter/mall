package drama.painter.core.web.asserts;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public class BaseException extends RuntimeException implements BaseEnum {
    static final int GLOBAL_ERROR_CODE = -999999;
    final int value;
    Object[] args;

    public BaseException(int value, String msg) {
        super(msg);
        this.value = value;
        if (value == GLOBAL_ERROR_CODE) {
            throw new RuntimeException("自定义错误代号不能为" + GLOBAL_ERROR_CODE);
        }
    }

    public BaseException(Throwable e, Object... args) {
        super(e);
        this.value = GLOBAL_ERROR_CODE;
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
