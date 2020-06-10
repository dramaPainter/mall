package drama.painter.web.rbac.model.eb;

import drama.painter.core.web.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    Integer id;
    String name;
    Integer pid;
    StatusEnum status;
}
