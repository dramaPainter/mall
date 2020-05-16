package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.model.oa.Staff;
import drama.painter.web.rbac.service.oa.IStaff;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Result<List<Staff>> staff(int page, Byte status, Byte key, String value) {
        return staff.list(page, status, key, value);
    }

    @PostMapping("/oa/staff/save")
    public Result staff_save(@RequestBody User u) {
        return staff.save(u);
    }

    @PostMapping("/oa/staff/remove")
    public Result staff_remove(@RequestBody int userid) {
        return staff.remove(userid);
    }

    @PostMapping("/oa/staff/avatar")
    public Result staff_avatar(@RequestBody String file) {
        return staff.avatar(file);
    }
}
