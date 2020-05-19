package drama.painter.web.rbac.service.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.model.oa.Role;
import drama.painter.web.rbac.model.oa.Staff;

import java.util.List;

/**
 * @author murphy
 */
public interface IStaff {
    /**
     * 查询员工资料表
     *
     * @param page   第几页
     * @param status 状态
     * @param key    搜索类型
     * @param value  搜索值
     * @return
     */
    Result<List<Staff>> list(int page, StatusEnum status, SearchEnum key, String value);

    /**
     * 添加或者更新员工资料
     *
     * @param staff 员工资料
     * @return
     */
    Result save(User staff);

    /**
     * 删除员工资料
     *
     * @param id 员工ID
     * @return
     */
    Result remove(int id);

    /**
     * 上传头像
     *
     * @param image 图片文件的BASE64字符
     * @return
     */
    Result avatar(String image);

    /**
     * 根据员工帐号查询员工资料
     *
     * @param username 员工帐号
     * @return
     */
    User getStaff(String username);
}
