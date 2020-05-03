package drama.painter.core.web.validator;

import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class BatchNumberValidator implements Validator {
    static final Pattern PATTERN = Pattern.compile("^(,\\d+)+$");

    @Override
    public boolean validate(String value) {
        value = trim(value);
        return !EMPTY.validate(value) && PATTERN.matcher(",".concat(value)).matches();
    }
}
