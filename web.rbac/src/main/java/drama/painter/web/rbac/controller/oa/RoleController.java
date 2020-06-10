package drama.painter.web.rbac.controller.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.oa.Role;
import drama.painter.web.rbac.service.intf.oa.IRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class RoleController {
    final IRole role;

    public RoleController(IRole role) {
        this.role = role;
    }

    @GetMapping("/oa/role")
    public Result<List<Role>> role(int page, int pageSize, StatusEnum status, SearchEnum key, String value) {
        return role.list(page, pageSize, status, key, value);
    }

    @PostMapping("/oa/role/save")
    public Result role_save(@RequestBody Role r) {
         role.save(r);
         return Result.SUCCESS;
    }

    @PostMapping("/oa/role/remove")
    public Result role_remove(@RequestBody int id) {
        role.remove(String.valueOf(id));
        return Result.SUCCESS;
    }
}
