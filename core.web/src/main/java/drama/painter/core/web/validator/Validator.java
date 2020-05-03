package drama.painter.core.web.validator;

/**
 * @author murphy
 */
public interface Validator {
    Validator EMPTY = new EmptyValidator();

    /**
     * 检查是否合法
     *
     * @param value 给出的值
     * @return
     */
    boolean validate(String value);

    /**
     * 执行trim方法
     *
     * @param value 一个string值
     * @return
     */
    default String trim(String value) {
        return value == null ? null : value.trim();
    }
}
