package drama.painter.web.rbac.model.oa;

import drama.painter.core.web.enums.StatusEnum;
import lombok.Data;

import java.util.List;

/**
 * @author murphy
 */
@Data
public class Staff {
    Integer id;
    String name;
    String alias;
    String avatar;
    List<String> role;
    StatusEnum status;
}
