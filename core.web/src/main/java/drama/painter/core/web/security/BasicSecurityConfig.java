package drama.painter.core.web.security;

import drama.painter.core.web.misc.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 使用BASIC验证模式
 *
 * @author murphy
 */
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.cloud.config.username}")
    String username;

    @Value("${spring.cloud.config.password}")
    String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.authorizeRequests().antMatchers("/**").authenticated().and().httpBasic().and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        auth.inMemoryAuthentication().withUser(new PageUserDetails(user)).passwordEncoder(new BCryptPasswordEncoder());
    }
}
