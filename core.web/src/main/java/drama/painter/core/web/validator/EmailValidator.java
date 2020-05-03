package drama.painter.core.web.validator;

import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class EmailValidator implements Validator {
    static final Pattern PATTERN = Pattern.compile("^([a-zA-Z0-9_.\\-])+@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");

    @Override
    public boolean validate(String value) {
        value = trim(value);
        return !EMPTY.validate(value) && PATTERN.matcher(value).matches();
    }
}
