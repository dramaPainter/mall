package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.oa.Role;
import drama.painter.web.rbac.service.oa.IRole;
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
    public Result<List<Role>> role(int page, Byte status, Byte key, String value) {
        return role.list(page, status, key, value);
    }

    @PostMapping("/oa/role/save")
    public Result staff_save(@RequestBody Role r) {
        return role.save(r);
    }

    @PostMapping("/oa/role/remove")
    public Result role_remove(@RequestBody int id) {
        return role.remove(id);
    }
}
