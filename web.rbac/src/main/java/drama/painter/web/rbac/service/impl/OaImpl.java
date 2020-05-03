package drama.painter.web.rbac.service.impl;

import drama.painter.core.web.ftp.upload.IUpload;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Encrypts;
import drama.painter.core.web.utility.Randoms;
import drama.painter.web.rbac.mapper.OaMapper;
import drama.painter.web.rbac.model.oa.Operation;
import drama.painter.web.rbac.model.oa.Staff;
import drama.painter.web.rbac.service.IOa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Slf4j(topic = "api")
@Service
public class OaImpl implements IOa {
    static final User USER = new User();
    static final Permission QUALIFY = new Permission();
    static final List<User> STAFF = new ArrayList();
    static final List<Permission> PERMISSION = new ArrayList();
    final OaMapper oaMapper;
    final IUpload upload;

    @Autowired
    public OaImpl(OaMapper oaMapper, IUpload upload) {
        this.oaMapper = oaMapper;
        this.upload = upload;
        STAFF.addAll(oaMapper.getStaff());
        PERMISSION.addAll(oaMapper.getPermission());
    }

    @Override
    public Result uploadAvatar(String image) {
        return upload.upload(image, "/head/{uuid}.jpg", 0);
    }

    @Override
    public Result<List<Staff>> listStaffs(int page, byte status, byte key, String value) {
        List<User> list = STAFF.stream()
                .filter(o -> status == -1 || (status > -1 && o.getStatus().getValue() == status))
                .filter(o -> key == -1
                        || (key == 1 && o.getId().toString().equals(value))
                        || (key == 2 && o.getName().contains(value))
                        || (key == 3 && o.getAlias().contains(value)))
                .collect(Collectors.toList());

        int from = Math.max(page - 1, 0) * Constant.PAGE_SIZE;
        int size = list.size();

        List<Staff> users = list.stream()
                .skip(from)
                .limit(Constant.PAGE_SIZE)
                .map(o -> {
                    Staff staff = new Staff();
                    BeanUtils.copyProperties(o, staff);
                    return staff;
                })
                .collect(Collectors.toList());

        list.clear();
        return Result.toData(size, users);
    }

    @Override
    public User getStaff(String username) {
        return STAFF.stream()
                .filter(o -> o.getName().equals(username))
                .findAny()
                .orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result saveStaff(User staff) {
        if (staff.getId() > 0) {
            User me = getStaff(staff.getName());
            if (me == null) {
                throw new RuntimeException("没有找到此员工帐号");
            } else {
                staff.setSalt(me.getSalt());
                if (StringUtils.isEmpty(staff.getPassword())) {
                    staff.setPassword(me.getPassword());
                } else if (!staff.getPassword().equals(me.getPassword())) {
                    staff.setPassword(Encrypts.md5(staff.getPassword() + me.getSalt()));
                }
            }
        } else {
            staff.setSalt(Randoms.getRandom(8));
            staff.setPassword(Encrypts.md5(staff.getPassword() + staff.getSalt()));
        }

        if (staff.getId() > 0) {
            oaMapper.updateStaff(staff);
        } else {
            oaMapper.addStaff(staff);
        }

        reset();
        return Result.SUCCESS;
    }

    @Override
    public List<Permission> getPermission() {
        return PERMISSION;
    }

    @Override
    public String getStaffPermission(int userid) {
        return StringUtils.collectionToCommaDelimitedString(STAFF.stream()
                .filter(o -> o.getId() == userid)
                .findAny()
                .orElse(new User())
                .getPermission());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result saveStaffPermission(int userid, List<String> permission) {
        oaMapper.removeStaffPermission(userid);
        if (!permission.isEmpty()) {
            oaMapper.saveStaffPermission(userid, permission);
        }
        reset();
        return Result.SUCCESS;
    }

    @Override
    public boolean hasPermission(int userid, String url) {
        List<String> permission = STAFF.stream()
                .filter(o -> o.getId().intValue() == userid)
                .findAny()
                .orElse(USER)
                .getPermission();

        if (permission.isEmpty()) {
            return false;
        } else {
            String id = String.valueOf(PERMISSION.stream()
                    .filter(o -> o.getUrl().equals(url))
                    .findAny()
                    .orElse(QUALIFY)
                    .getId());

            if (id == null) {
                return false;
            } else {
                return permission.stream().anyMatch(o -> o.equals(id));
            }
        }
    }

    @Override
    public Result<List<Operation>> listOperations(int page, String startTime, String endTime, Integer timespan, String text) {
        return Result.toData(0, Collections.EMPTY_LIST);
    }

    private void reset() {
        synchronized (STAFF) {
            STAFF.clear();
            STAFF.addAll(oaMapper.getStaff());
        }

        synchronized (PERMISSION) {
            PERMISSION.clear();
            PERMISSION.addAll(oaMapper.getPermission());
        }
    }
}
