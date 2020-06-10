package drama.painter.web.rbac.service.intf.oa;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.model.oa.Operation;

import java.util.List;

/**
 * @author murphy
 */
public interface IOa {
    /**
     * 根据员工帐号查询员工资料
     *
     * @param username 员工帐号
     * @param password 经MD5加密后的密码
     * @return
     */
    User getStaff(String username, String password);

    /**
     * 查看员工是否有权限访问网址
     *
     * @param username 员工帐号
     * @param url      网址
     * @return
     */
    Result<Boolean> hasPermission(String username, String url);

    /**
     * 查询员工操作记录，多项查询请用空格隔开
     *
     * @param page      第几页
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param timespan  耗时超过多少秒
     * @param text      搜索内容
     * @return
     */
    Result<List<Operation>> listOperation(int page, String startTime, String endTime, Integer timespan, String text);
}
