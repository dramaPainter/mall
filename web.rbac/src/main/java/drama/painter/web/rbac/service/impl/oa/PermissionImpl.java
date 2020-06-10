package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Caches;
import drama.painter.web.rbac.mapper.oa.PermissionMapper;
import drama.painter.web.rbac.mapper.oa.RolePermissionMapper;
import drama.painter.web.rbac.service.intf.oa.IPermission;
import drama.painter.web.rbac.tool.AssertEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static drama.painter.web.rbac.service.intf.other.ICache.PERMISSION_REFERENCE;

/**
 * @author murphy
 */
@Service
public class PermissionImpl implements IPermission {
    final RolePermissionMapper rolePermissionMapper;
    final PermissionMapper permissionMapper;

    @Autowired
    public PermissionImpl(PermissionMapper p, RolePermissionMapper rp) {
        this.permissionMapper = p;
        this.rolePermissionMapper = rp;
    }

    @Override
    public Result<List<Permission>> list(int page, int pageSize, StatusEnum status, SearchEnum key, String value) {
        Page p = new Page(page, pageSize);
        List<String> ids = permissionMapper.list(p, status, key, value);
        List<Permission> list = Caches.getHash("Permission", ids, PERMISSION_REFERENCE, this::get);
        return Result.toData(p.getRowCount(), list);
    }

    @Override
    public Permission get(String id) {
        Permission permission = Caches.getHash("Permission", id, Permission.class);
        if (Objects.isNull(permission)) {
            permission = permissionMapper.get(id);
            if (Objects.nonNull(permission)) {
                Caches.setHash("Permission", id, permission, -1);
            }
        }
        return permission;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Permission permission) {
        permissionMapper.save(permission);
        Caches.removeHash("Permission", String.valueOf(permission.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void remove(String id) {
        Permission permission = Caches.getHash("Permission", id, Permission.class);
        AssertEnum.NOT_FOUND.doAssert(permission == null, "权限ID:" + id);
        permissionMapper.remove(permission.getId());
        rolePermissionMapper.removeByPermission(permission.getId());
        Caches.removeHash("Permission", id);
        Caches.remove("Role");
    }
}
