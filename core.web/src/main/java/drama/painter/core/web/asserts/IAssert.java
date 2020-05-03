package drama.painter.core.web.asserts;

/**
 * @author murphy
 */
public interface IAssert {
    /**
     * 条件判断假设是否成立
     *
     * @param condition 条件
     * @param args      参数
     * @return
     */
    default void doAssert(boolean condition, Object... args) {
        if (condition) {
            throw newException(args);
        }
    }

    /**
     * 条件判断假设是否成立
     *
     * @param t    异常对象
     * @param args 参数
     * @return
     */
    default void doAssert(Throwable t, Object... args) {
        throw newException(t, args);
    }

    /**
     * 创建一个Exception
     *
     * @param args 参数
     * @return
     */
    BaseException newException(Object... args);

    /**
     * 创建一个Exception
     *
     * @param args 参数
     * @param t    异常对象
     * @return
     */
    BaseException newException(Throwable t, Object... args);
}
