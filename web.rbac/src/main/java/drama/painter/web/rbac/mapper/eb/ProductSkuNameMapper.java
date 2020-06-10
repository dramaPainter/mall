package drama.painter.web.rbac.mapper.eb;

import drama.painter.web.rbac.model.eb.ProductSkuName;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface ProductSkuNameMapper {
    /**
     * 查询产品SKU属性名
     *
     * @param pid 商品ID
     * @return
     */
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "list", column = "id", many = @Many(select = "drama.painter.web.rbac.mapper.eb.ProductSkuValueMapper.list"))
    })
    @Select("SELECT id,pid,name,impact FROM eb_product_sku_name WHERE pid = #{pid}")
    List<ProductSkuName> list(int pid);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"<script>INSERT INTO eb_product_sku_name(id, pid, name, impact) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.id}, #{item.pid}, #{item.name}, #{item.impact})",
            "</foreach></script>"})
    void add(List<ProductSkuName> list);

    @Update({"<script> UPDATE eb_product_sku_name SET ",
            "<trim prefix='pid = case' suffix='end,'><foreach collection='list' item='item'>when id= #{item.id} then #{item.pid} </foreach></trim>",
            "<trim prefix='name = case' suffix='end,'><foreach collection='list' item='item'>when id= #{item.id} then #{item.name} </foreach></trim>",
            "<trim prefix='impact = case' suffix='end'><foreach collection='list' item='item'>when id= #{item.id} then #{item.impact} </foreach></trim>",
            "WHERE id IN<foreach collection='list' item='item' open='(' separator=',' close=')'>#{item.id}</foreach></script>"})
    void update(List<ProductSkuName> list);

    /**
     * 从产品SKU属性名删除非ID集合内的数据
     *
     * @param pid  产品ID
     * @param list ID集合
     */
    @Delete({"<script>DELETE FROM eb_product_sku_name WHERE pid = #{pid} AND id not IN ",
            "<foreach item='item' collection='list' open='(' separator=',' close=')'>#{item.id}</foreach></script>"})
    void removeNot(@Param("pid") int pid, @Param("list") List<ProductSkuName> list);
}
