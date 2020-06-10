package drama.painter.web.rbac.model.oa;

import lombok.Data;

import java.util.List;

/**
 * @author murphy
 */
@Data
public class Role {
    Integer id;
    String name;
    List<Integer> permission;
}
