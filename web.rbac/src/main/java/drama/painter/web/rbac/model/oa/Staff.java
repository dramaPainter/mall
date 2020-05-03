package drama.painter.web.rbac.model.oa;

import drama.painter.core.web.enums.PlatformEnum;
import drama.painter.core.web.enums.StaffTypeEnum;
import drama.painter.core.web.enums.StatusEnum;
import lombok.Data;

/**
 * @author murphy
 */
@Data
public class Staff {
    Integer id;
    String name;
    String alias;
    String avatar;
    StatusEnum status;
    PlatformEnum platform;
    StaffTypeEnum type;
}
