package drama.painter.web.rbac.tool;

import drama.painter.core.web.security.PageSecurityConfig;
import drama.painter.web.rbac.service.intf.oa.IOa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author murphy
 */
@Component
public class Config {
    @Component
    public static class PageSecurity extends PageSecurityConfig {
        @Autowired
        public PageSecurity(IOa oa, HttpServletRequest request) {
            permissionChecker = user -> oa.hasPermission(user.getName(), request.getRequestURI());
            userProvider = (a, b) -> oa.getStaff(a, b);
        }
    }
}

