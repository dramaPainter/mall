package drama.painter.core.web.misc;

import drama.painter.core.web.enums.StatusEnum;
import lombok.Data;

import java.util.List;

/**
 * @author murphy
 */
@Data
public class User {
    Integer id;
    String name;
    String alias;
    String avatar;
    List<Integer> role;
    StatusEnum status;
}
