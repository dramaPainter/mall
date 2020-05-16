package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Dates;
import drama.painter.web.rbac.model.oa.Operation;
import drama.painter.web.rbac.model.oa.Staff;
import drama.painter.web.rbac.service.oa.IOa;
import drama.painter.web.rbac.service.oa.IPermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class PermissionController {
    final IPermission permission;

    public PermissionController(IPermission permission) {
        this.permission = permission;
    }

    @GetMapping("/oa/permission")
    public Result<List<Permission>> permission(int page, int pageSize, Byte status, Byte key, String value) {
        return permission.list(page, pageSize, status, key, value);
    }

    @PostMapping("/oa/permission/save")
    public Result permission_save(@RequestBody Permission p) {
        return permission.save(p);
    }

    @PostMapping("/oa/permission/remove")
    public Result permission_remove(@RequestBody int id) {
        return permission.remove(id);
    }
}
