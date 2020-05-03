package drama.painter.core.web.security;

import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Encrypts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Function;

/**
 * @author murphy
 */
class PasswordAuth extends AbstractUserDetailsAuthenticationProvider implements UserDetailsService {
    final static ThreadLocal<User> USER = new ThreadLocal();
    Function<String, User> userProvider;

    protected static void destroy() {
        USER.remove();
    }

    public void config(Function<String, User> userProvider) {
        this.hideUserNotFoundExceptions = false;
        this.userProvider = userProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PageUserDetails(new User());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails detail, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        if (USER.get() == null) {
            USER.set(userProvider.apply(username));
        }

        User user = USER.get();
        String password = token.getCredentials().toString();
        if (user == null) {
            throw new LoginSecurityConfig.BadLoginException("帐号不存在");
        } else if (!user.getPassword().equals(Encrypts.md5(password + user.getSalt()))) {
            throw new LoginSecurityConfig.BadLoginException("帐号或密码错误");
        } else if (user.getStatus() == StatusEnum.DISABLE) {
            throw new LoginSecurityConfig.BadLoginException("帐号已被冻结");
        }

        return new PageUserDetails(user);
    }
}
