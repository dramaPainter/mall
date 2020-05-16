package drama.painter.web.rbac.mapper.oa;

import drama.painter.web.rbac.model.oa.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Select({"SELECT id, name, status, " ,
            "(SELECT GROUP_CONCAT(permission) FROM oa_role_permission sp WHERE sp.role = s.id) AS permission ",
            "FROM oa_role s ORDER BY id DESC"})
    List<Role> list();

    /**
     * 保存数据到角色表
     *
     * @param role 添加对象
     */
    @Insert("REPLACE INTO zero.oa_role(id, name, status) VALUES(#{id}, #{name}, #{status})")
    void save(Role role);

    /**
     * 从角色表删除数据
     *
     * @param id 角色ID
     */
    @Delete("DELETE FROM zero.oa_role WHERE id = #{id}")
    void remove(@Param("id") int id);
}
