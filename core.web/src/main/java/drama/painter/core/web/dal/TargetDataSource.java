package drama.painter.core.web.dal;

import java.lang.annotation.*;

/**
 * @author murphy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value();
}
