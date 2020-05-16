package drama.painter.core.web.security;

import drama.painter.core.web.misc.User;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.function.Consumer;

/**
 * 使用页面鉴权模式
 *
 * @author murphy
 */
public class PageSecurityConfig extends LoginSecurityConfig {
    protected Consumer<User> permissionChecker;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .authorizeRequests()
                .accessDecisionManager(new AccessAllowedHandlerImpl(permissionChecker));
    }
}

