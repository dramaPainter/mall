package drama.painter.core.web.validator;

import org.springframework.util.StringUtils;

/**
 * @author murphy
 */
public class EmptyValidator implements Validator {
    @Override
    public boolean validate(String value) {
        value = trim(value);
        return StringUtils.isEmpty(value);
    }
}

