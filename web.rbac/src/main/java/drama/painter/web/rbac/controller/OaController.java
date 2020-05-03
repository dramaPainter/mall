package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.validator.DateValidator;
import drama.painter.core.web.validator.Validator;
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
    static final Validator DATE_VALIDATOR = new DateValidator();
    final IOa oa;

    public OaController(IOa oa) {
        this.oa = oa;
    }

    @GetMapping("/oa/staff")
    public Result<List<Staff>> staff(int page, byte status, byte key, String value) {
        return oa.listStaffs(page, status, key, value);
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
        return Result.toData(0, oa.getPermission());
    }

    @GetMapping("/oa/permission/staff")
    public Result permission_staff(int userid) {
        return Result.toData(0, oa.getStaffPermission(userid));
    }

    @PostMapping("/oa/permission/staff/save")
    public Result permission_staff_save(@RequestBody User staff) {
        return oa.saveStaffPermission(staff.getId(), staff.getPermission());
    }

    @GetMapping("/oa/operation")
    public Result<List<Operation>> operation(int page, String startTime, String endTime, int timespan, String searchText) {
        startTime = Dates.modify(startTime, 0, Dates.DateTimeType.DATE_TIME_MILLIS, Dates.toDate() + " 00:00:00,000");
        endTime = Dates.modify(endTime, 0, Dates.DateTimeType.DATE_TIME_MILLIS, Dates.toDateTime() + ",000");
        return oa.listOperations(page, startTime, endTime, timespan, searchText);
    }
}
