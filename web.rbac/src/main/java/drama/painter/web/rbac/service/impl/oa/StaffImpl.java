package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.ftp.upload.IUpload;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.security.LoginSecurityConfig;
import drama.painter.core.web.utility.Caches;
import drama.painter.web.rbac.mapper.oa.StaffMapper;
import drama.painter.web.rbac.mapper.oa.StaffRoleMapper;
import drama.painter.web.rbac.service.intf.oa.IStaff;
import drama.painter.web.rbac.tool.AssertEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static drama.painter.web.rbac.service.intf.other.ICache.STAFF_REFERENCE;

/**
 * @author murphy
 */
@Service
public class StaffImpl implements IStaff {
    final StaffMapper staffMapper;
    final StaffRoleMapper staffRoleMapper;
    final IUpload upload;

    @Autowired
    public StaffImpl(StaffMapper s, StaffRoleMapper sr, IUpload u) {
        this.staffMapper = s;
        this.staffRoleMapper = sr;
        this.upload = u;
    }

    @Override
    public Result<List<User>> list(int page, StatusEnum status, SearchEnum key, String value) {
        Page p = new Page(page, Constant.PAGE_SIZE);
        List<String> ids = staffMapper.list(p, status, key, value);
        List<User> list = Caches.getHash("Staff", ids, STAFF_REFERENCE, this::get);
        return Result.toData(p.getRowCount(), list);
    }

    @Override
    public User get(String username) {
        User staff = Caches.getHash("Staff", username, User.class);
        if (Objects.isNull(staff)) {
            staff = staffMapper.get(username);
            if (Objects.nonNull(staff)) {
                Caches.setHash("Staff", username, staff, -1);
            }
        }
        return staff;
    }

    @Override
    public User getStaff(String username, String password) {
        StatusEnum status = staffMapper.status(username, password);
        if (status == null) {
            throw new LoginSecurityConfig.BadLoginException("帐号或密码错误");
        } else if (status == StatusEnum.DISABLE) {
            throw new LoginSecurityConfig.BadLoginException("帐号已被冻结");
        }

        User staff = get(username);
        if (staff == null) {
            throw new LoginSecurityConfig.BadLoginException("帐号或密码错误：缓存未命中");
        }

        return staff;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(User staff) {
        if (Objects.nonNull(staff.getId()) && staff.getId() > 0) {
            staffMapper.update(staff);
        } else {
            staffMapper.add(staff);
        }

        staffRoleMapper.removeByStaff(staff.getId());
        if (Objects.nonNull(staff.getRole()) && staff.getRole().size() > 0) {
            staffRoleMapper.add(staff.getId(), staff.getRole());
        }

        Caches.removeHash("Staff", staff.getName());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void remove(String username) {
        User me = get(username);
        AssertEnum.NOT_FOUND.doAssert(me == null, "员工:" + username);
        staffMapper.remove(me.getId());
        staffRoleMapper.removeByStaff(me.getId());
        Caches.removeHash("Staff", username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setPassword(String username, String password) {
        staffMapper.updatePassword(username, password);
    }
}
