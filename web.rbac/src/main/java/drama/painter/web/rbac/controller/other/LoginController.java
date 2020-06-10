package drama.painter.web.rbac.controller.other;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.security.PageUserDetails;
import drama.painter.web.rbac.service.intf.oa.IOa;
import drama.painter.web.rbac.service.intf.oa.IPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author murphy
 */
@Controller
public class LoginController {
    final IOa oa;

    public LoginController(IOa oa) {this.oa = oa;}

    @GetMapping("/dir/login")
    public String login() {
        return "dir/login";
    }

    @GetMapping("/dir/error")
    public String error() {
        return "dir/error";
    }

    @ResponseBody
    @GetMapping("/dir/qualify")
    public Result qualify(String url) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            return Result.toData(Result.SUCCESS.getCode(), false);
        } else {
            String username = ((PageUserDetails) principal).getUser().getName();
            return oa.hasPermission(username, url);
        }
    }
}
