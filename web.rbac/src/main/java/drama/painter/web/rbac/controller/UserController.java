package drama.painter.web.rbac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author murphy
 */
@Controller
public class UserController {
    @GetMapping("/user/detail")
    public String detail() {
        return "user/detail";
    }

    @GetMapping("/user/deny")
    @ResponseBody
    public String deny() {
        return "user/deny";
    }

    @GetMapping("/user/game")
    @ResponseBody
    public String game() {
        return "user/game";
    }
}
