package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.enums.MenuTypeEnum;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Caches;
import drama.painter.web.rbac.mapper.oa.PermissionMapper;
import drama.painter.web.rbac.mapper.oa.RolePermissionMapper;
import drama.painter.web.rbac.model.oa.RolePermission;
import drama.painter.web.rbac.service.oa.IPermission;
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
public class PermissionImpl implements IPermission {
    static final User USER = new User();
    static final Permission QUALIFY = new Permission();

    final RolePermissionMapper rolePermissionMapper;
    final PermissionMapper permissionMapper;

    @Autowired
    public PermissionImpl(PermissionMapper permissionMapper, RolePermissionMapper rolePermissionMapper) {
        this.permissionMapper = permissionMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public Result<List<Permission>> list(int page, int pageSize, Byte status, Byte key, String value) {
        List<Permission> cache = Caches.get(OaImpl.PERMISSION);
        List<Permission> list = cache.stream()
                .filter(o -> Objects.isNull(status) || o.getStatus().getValue() == status)
                .filter(o -> Objects.isNull(key)
                        || (key == 1 && o.getId().toString().equals(value))
                        || (key == 2 && o.getName().contains(value))
                        || (key == 3 && o.getUrl().contains(value)))
                .collect(Collectors.toList());

        int from = Math.max(page - 1, 0) * pageSize;
        int size = list.size();

        List<Permission> users = list.stream()
                .skip(from)
                .limit(pageSize)
                .collect(Collectors.toList());

        list.clear();
        return Result.toData(size, users);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result save(Permission p) {
        permissionMapper.save(p);
        OaImpl.reset();
        return Result.SUCCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result remove(int id) {
        List<Permission> cache = Caches.get(OaImpl.PERMISSION);
        boolean hasChild = cache.stream().anyMatch(o -> o.getPid().intValue() == id);

        if (hasChild) {
            return Result.toFail("请先删除子节点后再删此节点");
        } else {
            permissionMapper.remove(id);
            rolePermissionMapper.removeByPermission(id);

            Integer pid = id;
            boolean isOnlyChild = true;
            while (isOnlyChild) {
                final Integer old_pid = pid;
                Integer new_pid = cache.stream().filter(o -> o.getId().equals(old_pid)).findAny().orElse(QUALIFY).getPid();
                isOnlyChild = cache.stream().filter(o -> o.getPid().equals(new_pid)).count() == 1;
                if (isOnlyChild) {
                    pid = new_pid;
                    permissionMapper.remove(pid);
                    rolePermissionMapper.removeByPermission(pid);
                }
            }

            OaImpl.reset();
            return Result.SUCCESS;
        }
    }

    @Override
    public boolean hasPermission(int userid, String url) {
        List<User> cacheStaff = Caches.get(OaImpl.STAFF);
        List<String> role = cacheStaff.stream()
                .filter(o -> o.getId().intValue() == userid)
                .findAny()
                .orElse(USER)
                .getRole();

        if (role.isEmpty()) {
            return false;
        } else {
            List<Permission> cachePermission = Caches.get(OaImpl.PERMISSION);
            Integer id = cachePermission.stream()
                    .filter(o -> o.getType() == MenuTypeEnum.ITEM)
                    .filter(o -> o.getUrl().equals(url))
                    .findAny()
                    .orElse(QUALIFY)
                    .getId();

            if (id == null) {
                return false;
            } else {
                List<RolePermission> cacheRolePermission = Caches.get(OaImpl.ROLE_PERMISSION);
                List<Integer> authRole = cacheRolePermission.stream()
                        .filter(o -> o.getPermission().equals(id))
                        .map(RolePermission::getRole)
                        .distinct()
                        .collect(Collectors.toList());

                List<String> copy = authRole.stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList());

                copy.retainAll(role);
                if (copy.isEmpty()) {
                    return false;
                } else {
                    copy.clear();
                    return true;
                }
            }
        }
    }
}
