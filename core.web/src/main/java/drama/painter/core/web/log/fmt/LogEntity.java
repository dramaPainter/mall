package drama.painter.core.web.log.fmt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author murphy
 */
@AllArgsConstructor
@Data
public class LogEntity {
    String table;
    String id;
    Map<String, Object> map;
}
