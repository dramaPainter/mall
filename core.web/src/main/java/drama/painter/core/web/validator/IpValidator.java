package drama.painter.core.web.validator;

import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class IpValidator implements Validator {
    static final Pattern PATTERN = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");

    @Override
    public boolean validate(String value) {
        value = trim(value);
        return !EMPTY.validate(value) && PATTERN.matcher(value).matches();
    }
}
