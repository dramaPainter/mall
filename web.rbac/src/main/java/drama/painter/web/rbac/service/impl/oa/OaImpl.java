package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Caches;
import drama.painter.web.rbac.mapper.oa.PermissionMapper;
import drama.painter.web.rbac.mapper.oa.RoleMapper;
import drama.painter.web.rbac.mapper.oa.RolePermissionMapper;
import drama.painter.web.rbac.mapper.oa.StaffMapper;
import drama.painter.web.rbac.model.oa.Operation;
import drama.painter.web.rbac.service.oa.IOa;
import drama.painter.web.rbac.service.oa.IPermission;
import drama.painter.web.rbac.service.oa.IStaff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author murphy
 */
@Service
public class OaImpl implements IOa {
    static final String ROLE = "GLOBAL_ROLE";
    static final String STAFF = "GLOBAL_STAFF";
    static final String PERMISSION = "GLOBAL_PERMISSION";
    static final String ROLE_PERMISSION = "GLOBAL_ROLE_PERMISSION";

    static RolePermissionMapper rolePermissionMapper;
    static PermissionMapper permissionMapper;
    static RoleMapper roleMapper;
    static StaffMapper staffMapper;

    final IStaff staff;
    final IPermission permission;
    final HttpServletRequest request;

    @Autowired
    public OaImpl(IStaff staff, IPermission permission, HttpServletRequest request) {
        this.staff = staff;
        this.permission = permission;
        this.request = request;
    }

    @Autowired
    public void init(RolePermissionMapper rolePermissionMapper, PermissionMapper permissionMapper, RoleMapper roleMapper, StaffMapper staffMapper) {
        OaImpl.rolePermissionMapper = rolePermissionMapper;
        OaImpl.permissionMapper = permissionMapper;
        OaImpl.roleMapper = roleMapper;
        OaImpl.staffMapper = staffMapper;
        reset();
    }

    @Override
    public User getStaff(String username) {
        return staff.getStaff(username);
    }

    @Override
    public boolean hasPermission(int userid) {
        String url = request.getRequestURI();
        return permission.hasPermission(userid, url);
    }

    public static void reset() {
        Caches.remove(PERMISSION);
        Caches.add(PERMISSION, permissionMapper.list(), -1);
        Caches.remove(ROLE);
        Caches.add(ROLE, roleMapper.list(), -1);
        Caches.remove(STAFF);
        Caches.add(STAFF, staffMapper.list(), -1);
        Caches.remove(ROLE_PERMISSION);
        Caches.add(ROLE_PERMISSION, rolePermissionMapper.list(), -1);
    }

    @Override
    public Result<List<Operation>> listOperation(int page, String startTime, String endTime, Integer timespan, String text) {
        return Result.toData(Result.SUCCESS.getCode(), Collections.EMPTY_LIST);
    }
}
