package drama.painter.web.rbac.service.intf.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;

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
    Result<List<User>> list(int page, StatusEnum status, SearchEnum key, String value);

    /**
     * 添加或者更新员工资料
     *
     * @param staff 员工资料
     * @return
     */
    void save(User staff);

    /**
     * 删除员工资料
     *
     * @param username 员工帐号
     * @return
     */
    void remove(String username);

    /**
     * 根据员工帐号和密码查询员工资料
     *
     * @param username 员工帐号
     * @param password 经MD5加密后的密码
     * @return
     */
    User getStaff(String username, String password);

    /**
     * 根据员工帐号查询员工资料
     *
     * @param username 员工帐号
     * @return
     */
    User get(String username);

    /**
     * 根据员工帐号查询员工资料
     *
     * @param username 员工帐号
     * @param password 经MD5加密后的密码
     * @return
     */
    void setPassword(String username, String password);
}
