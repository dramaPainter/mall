package drama.painter.core.web.misc;

import drama.painter.core.web.enums.PlatformEnum;
import drama.painter.core.web.enums.StaffTypeEnum;
import drama.painter.core.web.enums.StatusEnum;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author murphy
 */
@Data
public class User {
    @NotNull(message = "员工ID不能为空")
    Integer id;
    String name;
    String alias;
    String avatar;
    StatusEnum status;
    PlatformEnum platform;
    StaffTypeEnum type;
    String password;
    String salt;

    @Getter
    List<String> permission;

    public void setPermission(String permission) {
        if (StringUtils.isEmpty(permission)) {
            this.permission = Collections.EMPTY_LIST;
        } else {
            this.permission = Arrays.asList(permission.split(","));
        }
    }
}
