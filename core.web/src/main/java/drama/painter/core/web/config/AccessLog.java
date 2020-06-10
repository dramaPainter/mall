package drama.painter.core.web.config;

import drama.painter.core.web.utility.Json;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
@Slf4j(topic = "http")
@Aspect
@Component
public class AccessLog {
    static final List<String> HEADER = Arrays.asList("X-FORWARDED-FOR", "X-Real-IpTable");
    static final ThreadLocal<LocalDateTime> TIME = new ThreadLocal();
    @Value("${spring.application.name}")
    String project;

    public static String getIp(HttpServletRequest request) {
        String name = HEADER.stream().filter(o -> request.getHeader(o) != null).findFirst().orElse(null);
        String value = name == null ? request.getRemoteAddr() : request.getHeader(name);
        return value == null ? "127.0.0.1" : (value.contains(",") ? value.split(",")[0] : value);
    }

    /**
     * 记录格式：{时间} - {项目} {页面加载时间} {登录帐号} {SESSION} {IP} - {页面网址} - {页面参数} - {执行结果}
     * 执行结果：打印由POST方法提交后返回的值(GET方法不打印)
     */
    public static void add(String project, long timespan, HttpServletRequest request, Object[] args, Object result) {
        String url = request.getRequestURI();
        String returnValue = "NULL";
        String parameter;

        if ("/dir/qualify".equals(url)) {
            return;
        } else if ("POST".equals(request.getMethod())) {
            returnValue = Json.toJsonString(result);
            parameter = StringUtils.arrayToCommaDelimitedString(args);
            parameter = parameter.contains("data:image/jpeg;base64") ? "Upload(上传Base64图片)" : parameter;
        } else {
            parameter = request.getParameterMap().entrySet().stream()
                    .map(p -> p.getKey() + "=" + StringUtils.arrayToCommaDelimitedString(p.getValue()))
                    .reduce((p1, p2) -> p1 + "&" + p2)
                    .orElse("NULL");
        }

        String ip = getIp(request);
        String sessionId = request.getSession().getId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth == null ? "NULL" : auth.getName();
        username = "anonymousUser".equals(username) ? "NULL" : username;

        log.info("{} {} {} {} {} - {} - {} - {}", project, timespan, username, sessionId, ip, url, parameter, returnValue);
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    void cut() {
        log.info("CUT #####");
    }

    @Before("cut()")
    public void before() {
        TIME.set(LocalDateTime.now());
    }

    @AfterReturning(value = "cut()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        long timespan = Duration.between(TIME.get(), LocalDateTime.now()).toMillis();
        TIME.remove();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        add(project, timespan, request, point.getArgs(), result);
    }
}
