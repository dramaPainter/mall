package drama.painter.core.web.security;

import drama.painter.core.web.config.AccessLog;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Strings;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static drama.painter.core.web.security.LoginSecurityConfig.LOGIN_URL;

class FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    final String project;

    public FailureHandler(String project) {
        this.project = project;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        PasswordAuth.destroy();
        String param = LOGIN_URL.concat("?error=").concat(Strings.urlencode(e.getMessage()));
        String know = String.format("username: %s, password: %s", request.getParameter(LoginSecurityConfig.USER_PARAMETER),
                logger.isDebugEnabled() ? request.getParameter(LoginSecurityConfig.PASSWORD_PARAMETER) : "******");
        AccessLog.add(project, 0, request, new Object[]{"NULL"}, Result.toFail("登录失败：" + e.getMessage(), know));
        response.sendRedirect(param);
    }
}
