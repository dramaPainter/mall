package drama.painter.web.rbac.mapper.com;

import drama.painter.core.web.enums.UploadEnum;
import drama.painter.core.web.misc.Page;
import drama.painter.web.rbac.model.com.Image;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface ImageMapper {
    /**
     * 查询图片列表
     *
     * @param pid 产品ID
     * @return
     */
    @Select("SELECT id, type, value, name, url FROM com_image WHERE value = #{pid} AND type = 2")
    List<Image> listProduct(Integer pid);

    /**
     * 查询图片列表
     *
     * @param type 搜索对象
     * @param name 名称
     * @return
     */
    @Select({"<script>SELECT id, type, value, name, url FROM com_image <where>",
            "<if test='value != null'> AND value = #{value}</if>",
            "<if test='type != null'> AND type = #{type}</if>",
            "<if test='name != null'> AND name like CONCAT('%', #{name}, '%')</if>",
            "</where> ORDER BY id DESC</script>"})
    List<Image> list(Page page, @Param("type") UploadEnum type, @Param("name") String name, @Param("value") Integer value);

    /**
     * 保存数据到产品SKU属性值
     *
     * @param list 保存对象
     */
    @Insert({"<script>INSERT INTO com_image(id, type, value, name, url) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.id}, #{item.type}, #{item.value}, #{item.name}, #{item.url})",
            "</foreach></script>"})
    void save(List<Image> list);


    /**
     * 保存数据到图片库
     *
     * @param list 保存对象
     */
    @Update({"<script> UPDATE com_image SET ",
            "<trim prefix='value = case' suffix='end'><foreach collection='list' item='item'>when id= #{item.id} then #{item.value} </foreach></trim>",
            "WHERE id IN<foreach collection='list' item='item' open='(' separator=',' close=')'>#{item.id}</foreach></script>"})
    void update(List<Image> list);

    /**
     * 从产品SKU属性值删除数据
     *
     * @param value 类别对应的ID值
     */
    @Delete("UPDATE com_image SET value = 0 WHERE value = #{value}")
    void reset(int value);

    /**
     * 从产品SKU属性值删除数据
     *
     * @param id 自增列
     */
    @Delete("DELETE FROM com_image WHERE id = #{id}")
    void remove(int id);
}
