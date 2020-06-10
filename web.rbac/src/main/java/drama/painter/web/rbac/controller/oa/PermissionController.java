package drama.painter.web.rbac.controller.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.service.intf.oa.IPermission;
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
    public Result<List<Permission>> permission(int page, int pageSize, StatusEnum status, SearchEnum key, String value) {
        return permission.list(page, pageSize, status, key, value);
    }

    @PostMapping("/oa/permission/save")
    public Result permission_save(@RequestBody Permission p) {
        permission.save(p);
        return Result.SUCCESS;
    }

    @PostMapping("/oa/permission/remove")
    public Result permission_remove(@RequestBody int id) {
        permission.remove(String.valueOf(id));
        return Result.SUCCESS;
    }
}
