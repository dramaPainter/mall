package drama.painter.core.web.security;

import drama.painter.core.web.config.AccessLog;
import drama.painter.core.web.misc.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    String project;

    public SuccessHandler(String project) {
        this.project = project;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        PasswordAuth.destroy();
        super.onAuthenticationSuccess(request, response, authentication);
        AccessLog.add(project, 0, request, authentication.getName(), Result.toSuccess("登录成功"));
    }
}
