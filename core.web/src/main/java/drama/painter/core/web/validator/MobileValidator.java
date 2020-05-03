package drama.painter.core.web.validator;

import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class MobileValidator implements Validator {
    static final Pattern PATTERN = Pattern.compile("^1\\d{10}$");

    @Override
    public boolean validate(String value) {
        value = trim(value);
        return !EMPTY.validate(value) && PATTERN.matcher(value).matches();
    }
}
