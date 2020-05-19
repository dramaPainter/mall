package drama.painter.web.rbac.service.impl.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.ftp.upload.IUpload;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Caches;
import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.utility.Encrypts;
import drama.painter.core.web.utility.Randoms;
import drama.painter.web.rbac.mapper.oa.StaffMapper;
import drama.painter.web.rbac.mapper.oa.StaffRoleMapper;
import drama.painter.web.rbac.model.oa.Staff;
import drama.painter.web.rbac.service.oa.IStaff;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Service
public class StaffImpl implements IStaff {
    final StaffMapper staffMapper;
    final StaffRoleMapper staffRoleMapper;
    final IUpload upload;

    @Autowired
    public StaffImpl(StaffMapper staffMapper, StaffRoleMapper staffRoleMapper, IUpload upload) {
        this.staffMapper = staffMapper;
        this.staffRoleMapper = staffRoleMapper;
        this.upload = upload;
    }

    @Override
    public Result avatar(String image) {
        String path = String.format("/upload/head/%s/%s.jpg", Dates.toDate().substring(0, 7), UUID.randomUUID().toString());
        return upload.upload(image, path);
    }

    @Override
    public Result<List<Staff>> list(int page, StatusEnum status, SearchEnum key, String value) {
        List<User> cache = Caches.get(OaImpl.STAFF);
        List<User> list = cache.stream()
                .filter(o -> Objects.isNull(status) || o.getStatus() == status)
                .filter(o -> Objects.isNull(key)
                        || (key == SearchEnum.ID && o.getId().toString().equals(value))
                        || (key == SearchEnum.NAME && o.getName().contains(value))
                        || (key == SearchEnum.ALIAS && o.getAlias().contains(value)))
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
        List<User> cache = Caches.get(OaImpl.STAFF);
        return cache.stream()
                .filter(o -> o.getName().equals(username))
                .findAny()
                .orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result save(User staff) {
        if (Objects.nonNull(staff.getId()) && staff.getId() > 0) {
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
            staffMapper.update(staff);
        } else {
            staff.setSalt(Randoms.getRandom(8));
            staff.setPassword(Encrypts.md5(staff.getPassword() + staff.getSalt()));
            staffMapper.add(staff);
        }

        staffRoleMapper.removeByStaff(staff.getId());
        if (Objects.nonNull(staff.getRole()) && staff.getRole().size() > 0) {
            staffRoleMapper.add(staff.getId(), staff.getRole());
        }

        OaImpl.reset();
        return Result.SUCCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result remove(int userid) {
        staffMapper.remove(userid);
        staffRoleMapper.removeByStaff(userid);
        OaImpl.reset();
        return Result.SUCCESS;
    }
}
