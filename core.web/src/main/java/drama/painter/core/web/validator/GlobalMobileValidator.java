package drama.painter.core.web.validator;

import java.util.regex.Pattern;

/**
 * @author murphy
 */
public class GlobalMobileValidator implements Validator {
    static final MobileValidator CHINA = new MobileValidator();
    static final Pattern PATTERN = Pattern.compile("^\\(+\\d{1,3}\\)\\d{5,11}$");

    @Override
    public boolean validate(String value) {
        value = trim(value);
        return !EMPTY.validate(value) && (value.startsWith("(+86)") ? CHINA.validate(value.replace("(+86)", "")) : PATTERN.matcher(value).matches());
    }
}
