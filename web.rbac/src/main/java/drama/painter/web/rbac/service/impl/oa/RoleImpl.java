package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Caches;
import drama.painter.web.rbac.mapper.oa.RoleMapper;
import drama.painter.web.rbac.mapper.oa.RolePermissionMapper;
import drama.painter.web.rbac.mapper.oa.StaffRoleMapper;
import drama.painter.web.rbac.model.oa.Role;
import drama.painter.web.rbac.service.oa.IRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Service
public class RoleImpl implements IRole {
    final RoleMapper roleMapper;
    final StaffRoleMapper staffRoleMapper;
    final RolePermissionMapper rolePermissionMapper;

    @Autowired
    public RoleImpl(RoleMapper roleMapper, StaffRoleMapper staffRoleMapper, RolePermissionMapper rolePermissionMapper) {
        this.roleMapper = roleMapper;
        this.staffRoleMapper = staffRoleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Result<List<Role>> list(int page, Byte status, Byte key, String value) {
        List<Role> cache = Caches.get(OaImpl.ROLE);
        List<Role> list = cache.stream()
                .filter(o -> Objects.isNull(status) || o.getStatus().getValue() == status)
                .filter(o -> Objects.isNull(key)
                        || (key == 1 && o.getId().toString().equals(value))
                        || (key == 2 && o.getName().contains(value)))
                .collect(Collectors.toList());

        int from = Math.max(page - 1, 0) * Constant.PAGE_SIZE;
        int size = list.size();

        List<Role> roles = list.stream()
                .skip(from)
                .limit(Constant.PAGE_SIZE)
                .collect(Collectors.toList());

        list.clear();
        return Result.toData(size, roles);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result save(Role role) {
        roleMapper.save(role);
        rolePermissionMapper.removeByRole(role.getId());
        if (Objects.nonNull(role.getPermission()) && role.getPermission().size() > 0) {
            rolePermissionMapper.add(role.getId(), role.getPermission());
        }

        OaImpl.reset();
        return Result.SUCCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result remove(int id) {
        roleMapper.remove(id);
        staffRoleMapper.removeByRole(id);
        rolePermissionMapper.removeByRole(id);
        OaImpl.reset();
        return Result.SUCCESS;
    }
}
