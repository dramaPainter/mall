package drama.painter.core.web.security;

import drama.painter.core.web.misc.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.BiFunction;

/**
 * @author murphy
 */
class PasswordAuth extends AbstractUserDetailsAuthenticationProvider implements UserDetailsService {
    final static ThreadLocal<User> USER = new ThreadLocal();
    BiFunction<String, String, User> userProvider;

    protected static void destroy() {
        USER.remove();
    }

    public void config(BiFunction<String, String, User> userProvider) {
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
            USER.set(userProvider.apply(username, token.getCredentials().toString()));
        }

        // todo: ThreadLocal没有释放资源的地方
        return new PageUserDetails(USER.get());
    }
}
