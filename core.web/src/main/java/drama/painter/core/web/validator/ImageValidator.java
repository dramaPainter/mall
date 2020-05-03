package drama.painter.core.web.validator;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
public class ImageValidator implements Validator {
    static final List<String> EXT = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

    @Override
    public boolean validate(String value) {
        final String val = value == null ? "" : trim(value).toLowerCase();
        return EXT.stream().anyMatch(val::endsWith);
    }
}

