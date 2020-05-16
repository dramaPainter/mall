package drama.painter.web.rbac.tool;

import drama.painter.core.web.security.PageSecurityConfig;
import drama.painter.web.rbac.service.oa.IOa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author murphy
 */
@Component
public class Config {
    @Component
    public static class PageSecurity extends PageSecurityConfig {
        @Autowired
        public PageSecurity(IOa oa) {
            permissionChecker = user -> oa.hasPermission(user.getId());
            userProvider = username -> oa.getStaff(username);
        }
    }
}

