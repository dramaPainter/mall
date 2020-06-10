package drama.painter.web.rbac.mapper.oa;

import drama.painter.web.rbac.model.oa.RolePermission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface RolePermissionMapper {
    /**
     * 指定角色下的权限列表
     * @param role 角色ID
     * @return
     */
    @Select("SELECT permission FROM oa_role_permission WHERE role = #{role}")
    List<Integer> list(int role);

    /**
     * 添加角色权限
     *
     * @param role 角色ID
     * @param permission 权限ID
     */
    @Insert({"<script>INSERT INTO oa_role_permission (role, permission) VALUES ",
            "<foreach item='item' collection='permission' separator=','>",
            "(#{role}, #{item})",
            "</foreach></script>"})
    void add(@Param("role") int role, @Param("permission") List<Integer> permission);

    /**
     * 删除角色权限
     *
     * @param permission 权限ID
     */
    @Delete("DELETE FROM oa_role_permission WHERE permission = #{permission}")
    void removeByPermission(int permission);

    /**
     * 删除角色权限
     *
     * @param role 角色ID
     */
    @Delete("DELETE FROM oa_role_permission WHERE role = #{role}")
    void removeByRole(int role);
}
