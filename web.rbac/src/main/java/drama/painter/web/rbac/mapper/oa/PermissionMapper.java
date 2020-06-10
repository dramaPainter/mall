package drama.painter.web.rbac.mapper.oa;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.model.oa.Role;
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
    @Select("<script>SELECT id FROM oa_permission <trim prefix='WHERE' suffixOverrides='AND'>" +
            "<if test='key != null and key.value == 1'>id = #{value}</if>" +
            "<if test='key != null and key.value == 2'>name like CONCAT('%',#{value},'%')</if>" +
            "<if test='key != null and key.value == 5'>url = #{value}</if>" +
            "</trim>ORDER BY id DESC</script>")
    List<String> list(Page page, @Param("status") StatusEnum status, @Param("key") SearchEnum key, @Param("value") String value);


    @Select("SELECT id, name, url, pid, type, sort FROM oa_permission WHERE id = #{id}")
    Permission get(String id);

    /**
     * 添加数据到权限表
     *
     * @param permission 添加对象
     */
    @Insert({"REPLACE INTO oa_permission(id, name, url, pid, type, sort) VALUES",
            "(#{id}, #{name}, #{url}, #{pid}, #{type}, #{sort})"})
    void save(Permission permission);

    /**
     * 从权限表删除数据
     *
     * @param id 权限ID
     */
    @Delete("DELETE FROM oa_permission WHERE id = #{id}")
    void remove(int id);
}
