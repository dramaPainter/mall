package drama.painter.web.rbac.mapper.oa;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface PermissionMapper {
    /**
     * 查询所有权限
     *
     * @return
     */
    @Select("SELECT id, name, url, pid, type, sort, status FROM oa_permission")
    List<Permission> list();

    /**
     * 添加数据到权限表
     *
     * @param permission 添加对象
     */
    @Insert({"REPLACE INTO oa_permission(id, name, url, pid, type, sort, status) VALUES",
            "(#{id}, #{name}, #{url}, #{pid}, #{type}, #{sort}, #{status})"})
    void save(Permission permission);

    /**
     * 从权限表删除数据
     *
     * @param id 权限ID
     */
    @Delete("DELETE FROM oa_permission WHERE id = #{id}")
    void remove(int id);
}
