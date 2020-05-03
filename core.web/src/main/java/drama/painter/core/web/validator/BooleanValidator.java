package drama.painter.core.web.validator;

/**
 * @author murphy
 */
public class BooleanValidator implements Validator {
    @Override
    public boolean validate(String value) {
        value = trim(value);
        if (EMPTY.validate(value)) {
            return false;
        }
        return "true".equals(value) || "false".equals(value);
    }
}
