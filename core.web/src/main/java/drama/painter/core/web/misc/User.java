package drama.painter.core.web.misc;

import drama.painter.core.web.enums.StatusEnum;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    List<String> role;
    String salt;
    String password;
    StatusEnum status;

    @Setter
    public void setRole(String role) {
        if (!StringUtils.isEmpty(role)) {
            this.role = Arrays.asList(role.split(","));
        }
    }
}
