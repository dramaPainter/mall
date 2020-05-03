package drama.painter.core.web.validator;

import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class UrlValidator implements Validator {
    static final Pattern PATTERN = Pattern.compile("^https?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$");

    @Override
    public boolean validate(String value) {
        value = trim(value);
        return !EMPTY.validate(value) && PATTERN.matcher(value).matches();
    }
}

