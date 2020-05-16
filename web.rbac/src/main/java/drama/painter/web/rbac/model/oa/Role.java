package drama.painter.web.rbac.model.oa;

import drama.painter.core.web.enums.StatusEnum;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
@Data
public class Role {
    Integer id;
    String name;
    List<String> permission;
    StatusEnum status;

    @Setter
    public void setPermission(String permission) {
        if (!StringUtils.isEmpty(permission)) {
            this.permission = Arrays.asList(permission.split(","));
        }
    }
}
