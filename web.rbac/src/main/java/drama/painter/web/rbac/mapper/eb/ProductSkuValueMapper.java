package drama.painter.web.rbac.mapper.eb;

import drama.painter.web.rbac.model.eb.ProductSkuName;
import drama.painter.web.rbac.model.eb.ProductSkuValue;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface ProductSkuValueMapper {
    /**
     * 查询产品SKU属性值
     *
     * @param nameId SKU属性ID
     * @return
     */
    @Select("SELECT id,name_id,value FROM eb_product_sku_value WHERE name_id = #{nameId}")
    List<ProductSkuValue> list(int nameId);

    /**
     * 保存数据到产品SKU属性值
     *
     * @param list 保存对象
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"<script>REPLACE INTO eb_product_sku_value(id, name_id, value) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.id}, #{item.nameId}, #{item.value})",
            "</foreach></script>"})
    void save(List<ProductSkuValue> list);

    /**
     * 从产品SKU属性值删除数据
     *
     * @param list 属性ID集合
     */
    @Delete({"<script>DELETE FROM eb_product_sku_value WHERE name_id IN ",
            "<foreach item='item' collection='origin' open='(' separator=',' close=')'>#{item.id}</foreach>",
            "AND id NOT IN",
            "<foreach item='item' collection='list' open='(' separator=',' close=')'><if test='item.id != null'>#{item.id}</if></foreach></script>"})
    void removeNot(@Param("origin") List<ProductSkuName> origin, @Param("list") List<ProductSkuValue> list);



    /**
     * 从产品SKU属性值删除数据
     *
     * @param list 属性ID集合
     */
    @Delete({"<script>DELETE FROM eb_product_sku_value WHERE id IN ",
            "<foreach item='item' collection='list' open='(' separator=',' close=')'>#{item.id}</foreach></script>"})
    void remove(List<ProductSkuValue> list);
}
