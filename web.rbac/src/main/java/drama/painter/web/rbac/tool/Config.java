package drama.painter.web.rbac.tool;

import drama.painter.core.web.security.PageSecurityConfig;
import drama.painter.web.rbac.service.intf.oa.IOa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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

    @Bean
    public CorsFilter corsFilter() {
        //1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin("*");
        //是否发送 Cookie
        config.setAllowCredentials(true);
        //放行哪些请求方式
        config.addAllowedMethod("*");
        //放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        //2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        //3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }
}

