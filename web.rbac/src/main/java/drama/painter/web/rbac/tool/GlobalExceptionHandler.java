package drama.painter.web.rbac.tool;

import drama.painter.core.web.misc.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Controller增强器
 *
 * @author jim
 * @date 2017/11/23
 */
@Slf4j(topic = "api")
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 设置要捕获的异常，并作出处理
     * 注意：这里可以返回试图，也可以放回JSON，这里就当做一个Controller使用
     *
     * @param e {@link Exception}
     * @return {@link Map}
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handler(Exception e) {
        log.error("未处理的错误", e);
        return Result.toMessage(-1, e.getMessage());
    }
}
