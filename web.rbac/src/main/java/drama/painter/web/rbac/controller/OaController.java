package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Dates;
import drama.painter.web.rbac.model.oa.Operation;
import drama.painter.web.rbac.model.oa.Staff;
import drama.painter.web.rbac.service.IOa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class OaController {
    final IOa oa;

    public OaController(IOa oa) {
        this.oa = oa;
    }

    @GetMapping("/oa/staff")
    public Result<List<Staff>> staff(int page, byte status, byte key, String value) {
        return oa.listStaff(page, status, key, value);
    }

    @PostMapping("/oa/staff/save")
    public Result staff_save(@RequestBody User staff) {
        return oa.saveStaff(staff);
    }

    @PostMapping("/oa/staff/avatar")
    public Result staff_avatar(@RequestBody String file) {
        return oa.uploadAvatar(file);
    }

    @GetMapping("/oa/permission")
    public Result<List<Permission>> permission() {
        return oa.listPermission(1, 99999, (byte) -1, (byte) -1, "");
    }

    @GetMapping("/oa/permission/staff")
    public Result permission_staff(int userid) {
        return Result.toData(Result.SUCCESS.getCode(), oa.getStaffPermission(userid));
    }

    @PostMapping("/oa/permission/staff/save")
    public Result permission_staff_save(@RequestBody User staff) {
        return oa.saveStaffPermission(staff.getId(), staff.getPermission());
    }

    @GetMapping("/oa/operation")
    public Result<List<Operation>> operation(int page, String startTime, String endTime, int timespan, String searchText) {
        startTime = Dates.modify(startTime, 0, Dates.DateTimeType.DATE_TIME_MILLIS, Dates.toDate() + " 00:00:00,000");
        endTime = Dates.modify(endTime, 0, Dates.DateTimeType.DATE_TIME_MILLIS, Dates.toDateTime() + ",000");
        return oa.listOperation(page, startTime, endTime, timespan, searchText);
    }
}
