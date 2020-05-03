package drama.painter.core.web.validator;

/**
 * @author murphy
 */
public class IntegerValidator implements Validator {
    @Override
    public boolean validate(String value) {
        value = trim(value);
        if (value == null) {
            return false;
        } else {
            int sz = value.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(value.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
}
