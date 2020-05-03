package drama.painter.core.web.security;

import drama.painter.core.web.misc.Permission;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.List;
import java.util.function.Supplier;

/**
 * 使用页面鉴权模式
 *
 * @author murphy
 */
public class PageSecurityConfig extends LoginSecurityConfig {
    protected Supplier<List<Permission>> permissionProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .authorizeRequests()
                .accessDecisionManager(new AccessAllowedHandlerImpl(permissionProvider.get()));
    }
}

