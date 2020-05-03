package drama.painter.core.web.misc;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author murphy
 */
@AllArgsConstructor
@Data
public class Result<T> {
    static final int CODE_SUCCESS = 0;
    static final int CODE_FAIL = -1;
    static final String OPERATION_SUCCEED = "操作成功。";
    static final String OPERATION_FAILED = "操作失败，请稍候再重试。";

    public static Result SUCCESS = Result.toMessage(CODE_SUCCESS, OPERATION_SUCCEED);
    public static Result FAIL = Result.toMessage(CODE_FAIL, OPERATION_FAILED);
    int code;
    String message;
    T data;

    public Result() {
        this.code = CODE_FAIL;
        this.message = "状态进入初始化。";
    }

    public static <T> Result<T> toData(int code, T data) {
        return new Result(code, null, data);
    }

    public static <T> Result<T> toMessage(int code, String message) {
        return new Result(code, message, null);
    }

    public static <T> Result<T> toSuccess(String message) {
        return new Result(CODE_SUCCESS, message, null);
    }

    public static <T> Result<T> toFail(String message) {
        return new Result(CODE_FAIL, message, null);
    }

    public static <T> Result<T> toFail(String message, T data) {
        return new Result(CODE_FAIL, message, data);
    }
}
