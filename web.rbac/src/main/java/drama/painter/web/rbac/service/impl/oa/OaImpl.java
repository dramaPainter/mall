package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.model.oa.Operation;
import drama.painter.web.rbac.service.intf.oa.IOa;
import drama.painter.web.rbac.service.intf.oa.IPermission;
import drama.painter.web.rbac.service.intf.oa.IRole;
import drama.painter.web.rbac.service.intf.oa.IStaff;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author murphy
 */
@Service
public class OaImpl implements IOa {
    final IStaff staff;
    final IRole role;
    final IPermission permission;

    public OaImpl(IStaff s, IPermission p, IRole r) {
        this.staff = s;
        this.permission = p;
        this.role = r;
    }

    @Override
    public User getStaff(String username, String password) {
        return staff.getStaff(username, password);
    }

    @Override
    public Result<Boolean> hasPermission(String username, String url) {
        User staff = this.staff.get(username);
        boolean hasRight = staff.getRole().stream()
                .anyMatch(r -> role.get(String.valueOf(r)).getPermission().stream()
                        .anyMatch(p -> permission.get(String.valueOf(p)).getUrl().equals(url)));
        return new Result(Result.SUCCESS.getCode(), hasRight ? "" : "您无权访问 " + url, hasRight);
    }

    @Override
    public Result<List<Operation>> listOperation(int page, String startTime, String endTime, Integer timespan, String text) {
        return Result.toData(Result.SUCCESS.getCode(), Collections.EMPTY_LIST);
    }
}
