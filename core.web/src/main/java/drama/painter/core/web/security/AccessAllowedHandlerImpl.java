package drama.painter.core.web.security;

import drama.painter.core.web.enums.MenuTypeEnum;
import drama.painter.core.web.misc.Permission;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static drama.painter.core.web.security.LoginSecurityConfig.AUTHORIZED_SUFFIX_ITEM;
import static drama.painter.core.web.security.LoginSecurityConfig.AUTHORIZED_URL_PATH;

class AccessAllowedHandlerImpl implements AccessDecisionManager {
    final List<Permission> pages;

    public AccessAllowedHandlerImpl(List<Permission> pages) {
        this.pages = pages;
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
            Permission p = pages.stream()
                    .filter(t -> t.getType() == MenuTypeEnum.ITEM)
                    .filter(t -> t.getUrl().toLowerCase().equals(url))
                    .findAny()
                    .orElse(null);

            if (Objects.nonNull(p)) {
                boolean present = auth.getAuthorities().stream().anyMatch(m -> m.getAuthority().equals("ROLE_" + p.getId()));
                if (!present) {
                    throw new AccessDeniedException("您无权访问(操作)" + p.getName());
                }
            } else {
                throw new AccessDeniedException("页面不存在。");
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
