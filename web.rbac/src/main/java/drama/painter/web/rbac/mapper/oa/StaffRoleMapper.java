package drama.painter.web.rbac.mapper.oa;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface StaffRoleMapper {
    /**
     * 添加员工角色
     *
     * @param staff 员工ID
     */
    @Insert({"<script>INSERT INTO oa_staff_role (staff, role) VALUES ",
            "<foreach item='item' collection='role' separator=','>",
            "(#{staff}, #{item})",
            "</foreach></script>"})
    void add(@Param("staff") int staff, @Param("role") List<String> role);

    /**
     * 删除员工角色
     *
     * @param staff 员工ID
     */
    @Delete("DELETE FROM oa_staff_role WHERE staff = #{staff}")
    void removeByStaff(int staff);

    /**
     * 删除员工角色
     *
     * @param role 角色ID
     */
    @Delete("DELETE FROM oa_staff_role WHERE role = #{role}")
    void removeByRole(int role);
}
