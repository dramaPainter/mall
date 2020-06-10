package drama.painter.web.rbac.service.intf.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.oa.Role;

import java.util.List;

/**
 * @author murphy
 */
public interface IRole {
    /**
     * 查询角色表
     *
     * @param page     第几页
     * @param pageSize 每页记录数
     * @param status   状态
     * @param key      搜索类型
     * @param value    搜索值
     * @return
     */
    Result<List<Role>> list(int page, int pageSize, StatusEnum status, SearchEnum key, String value);

    /**
     * 添加或者更新角色
     *
     * @param role 角色
     * @return
     */
    void save(Role role);

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return
     */
    void remove(String id);

    /**
     * 根据员工帐号查询员工资料
     *
     * @param id 角色ID
     * @return
     */
    Role get(String id);
}
