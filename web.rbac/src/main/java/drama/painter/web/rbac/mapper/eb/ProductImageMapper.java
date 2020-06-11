package drama.painter.web.rbac.mapper.eb;

import drama.painter.web.rbac.model.eb.ProductImage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface ProductImageMapper {
    /**
     * 查询产品SKU属性值
     *
     * @param pid 产品ID
     * @return
     */
    @Select("SELECT id, type, value, url FROM eb_image WHERE value = #{pid} AND type = 2")
    List<ProductImage> list(Integer pid);

    /**
     * 保存数据到产品SKU属性值
     *
     * @param list 保存对象
     */
    @Insert({"<script>INSERT INTO eb_image(id, type, value, url) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.id}, #{item.type}, #{item.value}, #{item.url})",
            "</foreach></script>"})
    void save(List<ProductImage> list);

    /**
     * 从产品SKU属性值删除数据
     *
     * @param id 自增列
     */
    @Delete("DELETE FROM eb_image WHERE id = #{id}")
    void remove(int id);
}
