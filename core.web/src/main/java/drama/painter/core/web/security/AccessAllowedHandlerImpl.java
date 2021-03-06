package drama.painter.core.web.security;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.function.Function;

import static drama.painter.core.web.security.LoginSecurityConfig.AUTHORIZED_SUFFIX_ITEM;
import static drama.painter.core.web.security.LoginSecurityConfig.AUTHORIZED_URL_PATH;

class AccessAllowedHandlerImpl implements AccessDecisionManager {
    final Function<User, Result<Boolean>> permissionChecker;

    public AccessAllowedHandlerImpl(Function<User, Result<Boolean>> permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    @Override
    public void decide(Authentication auth, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        String url = ((FilterInvocation) o).getRequest().getRequestURI().toLowerCase();
        boolean authorized = AUTHORIZED_SUFFIX_ITEM.stream().anyMatch(u -> "/".equals(url) || url.endsWith(u));
        if (authorized) {
            return;
        } else if (url.startsWith(AUTHORIZED_URL_PATH)) {
            return;
        }

        if (auth.getPrincipal() instanceof String) {
            throw new InsufficientAuthenticationException("您还没有登录。");
        } else {
            Result<Boolean> r = permissionChecker.apply(((PageUserDetails) auth.getPrincipal()).getUser());
            if (!Boolean.TRUE.equals(r.getData())) {
                throw new AccessDeniedException(r.getMessage());
            }
        }
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
