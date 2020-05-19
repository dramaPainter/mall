package drama.painter.web.rbac.service.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;

import java.util.List;

/**
 * @author murphy
 */
public interface IPermission {
    /**
     * 所有权限列表
     *
     * @param page     第几页
     * @param pageSize 页大小
     * @param status   状态
     * @param key      搜索类型
     * @param value    搜索值
     * @return
     */
    Result<List<Permission>> list(int page, int pageSize, StatusEnum status, SearchEnum key, String value);

    /**
     * 更新或新增权限
     *
     * @param p 权限对象
     * @return
     */
    Result save(Permission p);

    /**
     * 删除权限（无法恢复）
     *
     * @param id 权限ID
     */
    Result remove(int id);

    /**
     * 查看员工是否有权限访问网址
     *
     * @param userid 员工ID
     * @param url    网址
     * @return
     */
    boolean hasPermission(int userid, String url);
}
