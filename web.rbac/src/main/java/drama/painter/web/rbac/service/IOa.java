package drama.painter.web.rbac.service;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.model.oa.Operation;
import drama.painter.web.rbac.model.oa.Staff;

import java.util.List;

/**
 * @author murphy
 */
public interface IOa {
    /**
     * 所有员工资料列表
     *
     * @param page 第几页
     * @return
     */
    Result<List<Staff>> listStaffs(int page, byte status, byte key, String value);

    /**
     * 根据员工帐号查询员工资料
     *
     * @param username 员工帐号
     * @return
     */
    User getStaff(String username);

    /**
     * 添加或者更新员工资料
     *
     * @param staff 员工资料
     * @return
     */
    Result saveStaff(User staff);

    /**
     * 上传头像
     *
     * @param image 图片文件的BASE64字符
     * @return
     */
    Result uploadAvatar(String image);

    /**
     * 所有权限列表
     *
     * @return
     */
    List<Permission> getPermission();

    /**
     * 查询员工权限
     *
     * @param userid 员工ID
     * @return
     */
    String getStaffPermission(int userid);

    /**
     * 设置员工权限
     *
     * @param userid     员工ID
     * @param permission 权限项
     * @return
     */
    Result saveStaffPermission(int userid, List<String> permission);

    /**
     * 查看员工是否有权限访问网址
     *
     * @param userid 员工ID
     * @param url    网址
     * @return
     */
    boolean hasPermission(int userid, String url);

    /**
     * 查询员工操作记录，多项查询请用空格隔开
     *
     * @param page      第几页
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param timespan  耗时超过多少秒
     * @param project   项目名称
     * @param text      搜索内容
     * @return
     */
    Result<List<Operation>> listOperations(int page, String startTime, String endTime, Integer timespan, String text);
}
