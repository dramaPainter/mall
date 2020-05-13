package drama.painter.web.rbac.tool;

import drama.painter.core.web.enums.StaffTypeEnum;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.security.PageSecurityConfig;
import drama.painter.web.rbac.service.IOa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author murphy
 */
@Component
public class Config {
    @Component
    public static class PageSecurity extends PageSecurityConfig {
        static final List<StaffTypeEnum> CS = Arrays.asList(
                StaffTypeEnum.CS_OTHER,
                StaffTypeEnum.CS_RECHARGE,
                StaffTypeEnum.CS_EXCHANGE,
                StaffTypeEnum.CS_MANAGER
        );

        @Autowired
        public PageSecurity(IOa oa) {
            permissionProvider = () -> oa.listPermission(1, 99999, (byte) -1, (byte) -1, "").getData();
            userProvider = username -> {
                User staff = oa.getStaff(username);
                if (Objects.nonNull(staff) && CS.contains(staff.getType())) {
                    throw new LockedException("您登录的帐号不是后台帐号");
                }
                return staff;
            };
        }
    }
}

