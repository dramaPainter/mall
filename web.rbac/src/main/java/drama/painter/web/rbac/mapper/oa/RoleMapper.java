package drama.painter.web.rbac.mapper.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Page;
import drama.painter.web.rbac.model.oa.Role;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface RoleMapper {
    /**
     * 查询角色表
     *
     * @return
     */
    @Select("<script>SELECT id FROM oa_role <trim prefix='WHERE' suffixOverrides='AND'>" +
            "<if test='key != null and key.value == 1'>id = #{value}</if>" +
            "<if test='key != null and key.value == 2'>name like CONCAT('%',#{value},'%')</if>" +
            "</trim>ORDER BY id DESC</script>")
    List<String> list(Page page, @Param("status") StatusEnum status, @Param("key") SearchEnum key, @Param("value") String value);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "permission", column = "id", many = @Many(select = "drama.painter.web.rbac.mapper.oa.RolePermissionMapper.list"))
    })
    @Select("SELECT id, name, remark FROM oa_role WHERE id = #{id}")
    Role get(String id);

    /**
     * 保存数据到角色表
     *
     * @param role 添加对象
     */
    @Insert("INSERT INTO oa_role(id, name, remark) VALUES(#{id}, #{name}, #{remark}) " +
            "ON DUPLICATE KEY UPDATE name = VALUES(name), remark = VALUES(remark)")
    void save(Role role);

    /**
     * 从角色表删除数据
     *
     * @param id 角色ID
     */
    @Delete("DELETE FROM oa_role WHERE id = #{id}")
    void remove(@Param("id") int id);
}
