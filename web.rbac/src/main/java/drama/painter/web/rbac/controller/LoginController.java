package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.security.PageUserDetails;
import drama.painter.web.rbac.service.oa.IOa;
import drama.painter.web.rbac.service.oa.IPermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author murphy
 */
@Controller
public class LoginController {
    final IPermission permission;

    public LoginController(IPermission permission) {
        this.permission = permission;
    }

    @GetMapping("/login/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/login/error")
    public String error() {
        return "login/error";
    }

    @ResponseBody
    @GetMapping("/login/qualify")
    public Result qualify(String url) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            return Result.toData(Result.SUCCESS.getCode(), false);
        } else {
            int userid = ((PageUserDetails) principal).getUser().getId();
            return Result.toData(Result.SUCCESS.getCode(), permission.hasPermission(userid, url));
        }
    }
}
