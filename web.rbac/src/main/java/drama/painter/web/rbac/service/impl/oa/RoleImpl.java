package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Caches;
import drama.painter.web.rbac.mapper.oa.RoleMapper;
import drama.painter.web.rbac.mapper.oa.RolePermissionMapper;
import drama.painter.web.rbac.mapper.oa.StaffRoleMapper;
import drama.painter.web.rbac.model.oa.Role;
import drama.painter.web.rbac.service.intf.oa.IRole;
import drama.painter.web.rbac.tool.AssertEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static drama.painter.web.rbac.service.intf.other.ICache.ROLE_REFERENCE;

/**
 * @author murphy
 */
@Service
public class RoleImpl implements IRole {
    final RoleMapper roleMapper;
    final StaffRoleMapper staffRoleMapper;
    final RolePermissionMapper rolePermissionMapper;

    @Autowired
    public RoleImpl(RoleMapper r, StaffRoleMapper sr, RolePermissionMapper rp) {
        this.roleMapper = r;
        this.staffRoleMapper = sr;
        this.rolePermissionMapper = rp;
    }

    @Override
    public Result<List<Role>> list(int page, int pageSize, StatusEnum status, SearchEnum key, String value) {
        Page p = new Page(page, pageSize);
        List<String> ids = roleMapper.list(p, status, key, value);
        List<Role> list = Caches.getHash("Role", ids, ROLE_REFERENCE, this::get);
        return Result.toData(p.getRowCount(), list);
    }

    @Override
    public Role get(String id) {
        Role role = Caches.getHash("Role", id, Role.class);
        if (Objects.isNull(role)) {
            role = roleMapper.get(id);
            if (Objects.nonNull(role)) {
                Caches.setHash("Role", id, role, -1);
            }
        }
        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Role role) {
        roleMapper.save(role);
        if (Objects.nonNull(role.getPermission()) && role.getPermission().size() > 0) {
            rolePermissionMapper.removeByRole(role.getId());
            rolePermissionMapper.add(role.getId(), role.getPermission());
        }

        Caches.removeHash("Role", String.valueOf(role.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void remove(String id) {
        Role role = Caches.getHash("Role", id, Role.class);
        AssertEnum.NOT_FOUND.doAssert(role == null, "角色ID:" + id);
        roleMapper.remove(role.getId());
        staffRoleMapper.removeByRole(role.getId());
        rolePermissionMapper.removeByRole(role.getId());
        Caches.removeHash("Role", id);
        Caches.remove("Staff");
    }
}
