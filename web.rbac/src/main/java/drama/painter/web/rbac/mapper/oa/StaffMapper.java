package drama.painter.web.rbac.mapper.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Page;
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
    @Select("<script>SELECT name FROM oa_staff <trim prefix='WHERE' suffixOverrides='AND'>" +
            "<if test='status != null'>status = #{status}</if>" +
            "<if test='key != null and key.value == 1'>id = #{value}</if>" +
            "<if test='key != null and key.value == 2'>name like CONCAT('%',#{value},'%')</if>" +
            "<if test='key != null and key.value == 3'>alias like CONCAT('%',#{value},'%')</if>" +
            "</trim>ORDER BY id DESC</script>")
    List<String> list(Page page, @Param("status") StatusEnum status, @Param("key") SearchEnum key, @Param("value") String value);

    /**
     * 查询所有员工帐号
     *
     * @return
     */
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "role", column = "id", many = @Many(select = "drama.painter.web.rbac.mapper.oa.StaffRoleMapper.list"))
    })
    @Select("SELECT id, name, alias, status, avatar FROM oa_staff WHERE name = #{username}")
    User get(String username);

    /**
     * 帐号登录
     *
     * @param username 帐号
     * @param password 已用MD5加密的密码
     * @return
     */
    @Select("SELECT status FROM oa_staff WHERE name = #{username} AND password = UPPER(MD5(CONCAT(#{password}, salt)))")
    StatusEnum status(@Param("username") String username, @Param("password") String password);

    /**
     * 添加数据到员工表
     *
     * @param staff 添加对象
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"INSERT INTO oa_staff(id, name, alias, status, salt, password, avatar) VALUES",
            "(#{id}, #{name}, #{alias}, #{status}, '', '', #{avatar})"})
    void add(User staff);

    /**
     * 更新数据到员工表
     *
     * @param staff 更新对象
     */
    @Update({"<script>UPDATE oa_staff <trim prefix='UPDATE' suffixOverrides=','>",
            "<if test='name != null'>name = #{name}</if>",
            "<if test='alias != null'>alias = #{alias}</if>",
            "<if test='status != null'>status = #{status}</if>",
            "<if test='avatar != null'>avatar = #{avatar}</if>",
            "</trim><where>id = #{id}</where></script>"})
    void update(User staff);


    /**
     * 更新数据到员工表
     *
     * @param username 帐号
     * @param password 已用MD5加密的密码
     */
    @Update("UPDATE oa_staff SET password = UPPER(MD5(CONCAT(#{password}, salt))) WHERE name = #{username}")
    void updatePassword(@Param("username") String username, @Param("password") String password);

    /**
     * 从员工表删除数据
     *
     * @param id 帐号ID
     */
    @Delete("DELETE FROM oa_staff WHERE id = #{id}")
    void remove(int id);
}
