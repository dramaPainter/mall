package drama.painter.core.web.security;

import drama.painter.core.web.misc.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 使用FORM验证模式，登录成功后即可获得操作权限。
 * Web Security 执行顺序:
 * 第一步：执行retrieveUser方法。如果执行无误，没有后续步骤。
 * 第二步：执行loadUserByUsername方法。此方法只能抛出异常。
 * 第三步：再次回到retrieveUser方法。使用ThreadLocal避免重复读数据库。
 *
 * @author murphy
 */
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String AUTHORIZED_URL_PATH = "/login/";
    public static final String USER_PARAMETER = "loginUser";
    public static final String PASSWORD_PARAMETER = "loginPassword";

    public static final String[] AUTHORIZED_SUFFIX = {"/",
            "*.ico", "*.jpg", "*.jpeg", "*.png", "*.gif",
            "*.html", "*.htm", "*.xml", "*.js", "*.json",
            "*.css", "*.woff", "*.ttf", "*.svg",
            "*.rar", "*.zip", "*.gz", "*.ipa", "*.apk", "*.plist",
            "*.xls", "*.xlsx", "*.doc", "*.docx", "*.pdf",
            "*.mp3", "*.mp4", "*.mov", "*.ogg", "*.m3u",
            "*.log", "*.txt"
    };

    public static final List<String> AUTHORIZED_SUFFIX_ITEM = Arrays.stream(AUTHORIZED_SUFFIX)
            .filter(o -> !"/".equals(o))
            .map(o -> o.replace("*", ""))
            .collect(Collectors.toList());

    static final PasswordAuth PASSWORD_AUTH = new PasswordAuth();
    static final String LOGIN_URL = "/login/login";
    static final String SECRET_KEY = "Web-Security-Client-Key";
    protected Function<String, User> userProvider;

    @Value("${spring.application.name}")
    String project;

    @PostConstruct
    public void init() {
        PASSWORD_AUTH.config(userProvider);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and().csrf().disable()
                .authenticationProvider(PASSWORD_AUTH).authorizeRequests()
                .antMatchers(AUTHORIZED_SUFFIX).permitAll()
                .antMatchers(AUTHORIZED_URL_PATH + "**").permitAll()
                .anyRequest().authenticated()
                .and().rememberMe().rememberMeServices(getRememberMeServices())
                .and().logout().logoutUrl("/login/logout").logoutSuccessUrl("/")
                .invalidateHttpSession(true).clearAuthentication(true)
                .and().formLogin().usernameParameter(USER_PARAMETER).passwordParameter(PASSWORD_PARAMETER)
                .loginProcessingUrl("/login/security").loginPage(LOGIN_URL).defaultSuccessUrl("/")
                .successHandler(new SuccessHandler(project)).failureHandler(new FailureHandler(project))
                .permitAll();
    }

    TokenBasedRememberMeServices getRememberMeServices() {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(SECRET_KEY, PASSWORD_AUTH);
        services.setCookieName("remember");
        services.setParameter("remember");
        services.setTokenValiditySeconds(24 * 3600);
        return services;
    }

    public static class BadLoginException extends AuthenticationException {
        public BadLoginException(String msg) {
            super(msg);
        }
    }
}

