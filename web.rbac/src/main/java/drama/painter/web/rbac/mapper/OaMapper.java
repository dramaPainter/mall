package drama.painter.web.rbac.mapper;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface OaMapper {
    /**
     * 查询所有页面
     *
     * @return
     */
    @Select("SELECT * FROM zero.permission WHERE status = 1")
    List<Permission> getPermission();

    /**
     * 添加数据到权限表
     *
     * @param permission 添加对象
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"REPLACE INTO zero.permission(id, name, url, pid, type, sort, status) VALUES",
            "(#{id}, #{name}, #{url}, #{pid}, #{type}, #{sort}, #{status})"})
    void savePermission(@Param("permission") Permission permission);

    /**
     * 从权限表删除数据
     *
     * @param id 页面ID
     */
    @Delete("DELETE FROM zero.permission WHERE id = #{id}")
    void removePermission(@Param("id") int id);

    /**
     * 删除员工权限
     *
     * @param id 页面ID
     */
    @Delete("DELETE FROM zero.staff_permission WHERE permission = #{id}")
    void removePermissionOnStaff(int id);

    /**
     * 从权限表删除数据
     *
     * @param id 页面ID
     */
    @Select("SELECT COUNT(1) FROM zero.permission WHERE pid = #{id}")
    boolean hasChild(@Param("id") int id);

    /**
     * 查询所有员工帐号
     *
     * @return
     */
    @Select({"SELECT id, name, alias, password, salt, avatar, status, type, platform, ",
            "(SELECT GROUP_CONCAT(permission) FROM zero.staff_permission sp WHERE sp.userid = s.id) AS permission ",
            "FROM zero.staff s ORDER BY id DESC"})
    List<User> getStaff();

    /**
     * 添加员工资料
     *
     * @param staff 员工资料
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"INSERT INTO zero.staff (name, password, salt, alias, avatar, status, type, platform) VALUES ",
            "(#{name}, #{password}, #{salt}, #{alias}, #{avatar}, #{status}, #{type}, #{platform})"})
    void addStaff(User staff);

    /**
     * 更新员工资料
     *
     * @param staff 员工资料
     */
    @Update({"UPDATE zero.staff SET ",
            "password = #{password}, alias = #{alias}, avatar = #{avatar}, ",
            "status = #{status}, type = #{type}, platform = #{platform} ",
            "WHERE id = #{id}"})
    void updateStaff(User staff);

    /**
     * 设置员工权限
     *
     * @param userid 员工资料
     */
    @Insert({"<script>INSERT INTO zero.staff_permission (userid, permission) VALUES ",
            "<foreach item='item' collection='permission' separator=','>",
            "(#{userid}, #{item})",
            "</foreach></script>"})
    void saveStaffPermission(@Param("userid") int userid, @Param("permission") List<String> permission);

    /**
     * 删除员工权限
     *
     * @param userid 员工ID
     */
    @Delete("DELETE FROM zero.staff_permission WHERE userid = #{userid}")
    void removeStaffPermission(int userid);
}
