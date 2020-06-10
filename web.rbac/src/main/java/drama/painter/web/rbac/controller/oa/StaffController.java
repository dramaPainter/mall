package drama.painter.web.rbac.controller.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.service.intf.oa.IStaff;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author murphy
 */
@RestController
public class StaffController {
    final IStaff staff;

    public StaffController(IStaff staff) {
        this.staff = staff;
    }

    @GetMapping("/oa/staff")
    public Result<List<User>> staff(int page, StatusEnum status, SearchEnum key, String value) {
        return staff.list(page, status, key, value);
    }

    @PostMapping("/oa/staff/save")
    public Result staff_save(@RequestBody User u) {
        staff.save(u);
        return Result.SUCCESS;
    }

    @PostMapping("/oa/staff/remove")
    public Result staff_remove(@RequestBody String username) {
        staff.remove(username);
        return Result.SUCCESS;
    }

    @PostMapping("/oa/staff/password")
    public Result staff_password(@RequestBody Map<String, String> map) {
        staff.setPassword(map.get("username"), map.get("password"));
        return Result.SUCCESS;
    }
}
