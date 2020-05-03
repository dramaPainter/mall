package drama.painter.core.web.security;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Json;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    /**
     * 如果想要跳转页面，可以使用以下语句：
     * request.setAttribute("msg", e.getMessage());
     * request.getRequestDispatcher("/login/error").forward(request, response);
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(Json.toJsonString(Result.toMessage(-1, e.getMessage())));
    }
}
