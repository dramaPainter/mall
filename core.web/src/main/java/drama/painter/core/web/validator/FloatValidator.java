package drama.painter.core.web.validator;

import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class FloatValidator implements Validator {
    static final Pattern PATTERN = Pattern.compile("^\\d+\\.?\\d+$");

    @Override
    public boolean validate(String value) {
        value = trim(value);
        return !EMPTY.validate(value) && PATTERN.matcher(value).matches();
    }
}
