package drama.painter.web.rbac.mapper.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Page;
import drama.painter.web.rbac.model.eb.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface ProductMapper {
    /**
     * 查询产品列表
     *
     * @return
     */
    @Select("<script>SELECT code FROM eb_product <trim prefix='WHERE' suffixOverrides='AND'>" +
            "<if test='status != null'>status = #{status}</if>" +
            "<if test='key != null and key.value == 1'>id = #{value}</if>" +
            "<if test='key != null and key.value == 2'>name like CONCAT('%',#{value},'%')</if>" +
            "<if test='key != null and key.value == 5'>code = #{value}</if>" +
            "</trim> ORDER BY sort DESC</script>")
    List<String> list(Page page, @Param("status") StatusEnum status, @Param("key") SearchEnum key, @Param("value") String value);

    /**
     * 查询产品列表
     *
     * @return
     */
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "sku", column = "id", many = @Many(select = "drama.painter.web.rbac.mapper.eb.ProductSkuMapper.list")),
            @Result(property = "spu", column = "id", many = @Many(select = "drama.painter.web.rbac.mapper.eb.ProductSkuNameMapper.list")),
            @Result(property = "image", column = "id", many = @Many(select = "drama.painter.web.rbac.mapper.com.ImageMapper.listProduct"))
    })
    @Select("SELECT id,name,code,category,brand,sort,status,hottest,latest,sale,keyword,avatar,body FROM eb_product WHERE code = #{code}")
    Product get(String code);

    /**
     * 添加数据到产品列表
     *
     * @param p 添加对象
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert({"INSERT INTO eb_product(id, name, code, category, brand, sort, status, hottest, latest, sale, keyword, avatar, body) VALUES",
            "(#{id}, #{name}, #{code}, #{category}, #{brand}, #{sort}, #{status}, #{hottest}, #{latest}, #{sale}, #{keyword}, #{avatar}, #{body})"})
    void add(Product p);

    /**
     * 更新数据到产品列表
     *
     * @param p 更新对象
     */
    @Update({"UPDATE eb_product SET ",
            "name = #{name}, category = #{category}, brand = #{brand}, ",
            "sort = #{sort}, hottest = #{hottest}, latest = #{latest}, ",
            "sale = #{sale}, keyword = #{keyword}, avatar = #{avatar}, body = #{body} ",
            "WHERE id = #{id}"})
    void update(Product p);

    /**
     * 上架下架产品
     *
     * @param id 产品ID
     */
    @Delete("UPDATE eb_product SET status = #{status} WHERE id = #{id}")
    void display(@Param("id") int id, @Param("status") StatusEnum status);

    /**
     * 从产品列表删除数据
     *
     * @param id 产品ID
     */
    @Delete("DELETE FROM eb_product WHERE id = #{id}")
    void remove(int id);
}
