package drama.painter.web.rbac.mapper.oa;

import drama.painter.core.web.misc.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface StaffMapper {
    /**
     * 查询所有员工帐号
     *
     * @return
     */
    @Select({"SELECT id, name, alias, status, salt, password, avatar, ",
            "(SELECT GROUP_CONCAT(role) FROM oa_staff_role sp WHERE sp.staff = s.id) AS role ",
            "FROM oa_staff s ORDER BY id DESC"})
    List<User> list();

    /**
     * 添加数据到员工表
     *
     * @param staff 添加对象
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"INSERT INTO oa_staff(id, name, alias, status, salt, password, avatar) VALUES",
            "(#{id}, #{name}, #{alias}, #{status}, #{salt}, #{password}, #{avatar})"})
    void add(User staff);

    /**
     * 更新数据到员工表
     *
     * @param staff 更新对象
     */
    @Update({"<script>UPDATE oa_staff<set>",
            "<if test='name != null'>name = #{name},</if>",
            "<if test='alias != null'>alias = #{alias},</if>",
            "<if test='status != null'>status = #{status},</if>",
            "<if test='salt != null'>salt = #{salt},</if>",
            "<if test='password != null'>password = #{password},</if>",
            "<if test='avatar != null'>avatar = #{avatar},</if>",
            "</set><where>id = #{id}</where></script>"})
    void update(User staff);

    /**
     * 从员工表删除数据
     *
     * @param id 帐号ID
     */
    @Delete("DELETE FROM oa_staff WHERE id = #{id}")
    void remove(int id);
}
