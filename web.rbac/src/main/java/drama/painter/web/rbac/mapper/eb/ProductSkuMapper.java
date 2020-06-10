package drama.painter.web.rbac.mapper.eb;

import drama.painter.web.rbac.model.eb.ProductSku;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface ProductSkuMapper {
    /**
     * 查询产品SKU
     *
     * @param pid 商品ID
     * @return
     */
    @Select("SELECT pid,combo,sn,stock,retail,market,purchase FROM eb_product_sku where pid = #{pid}")
    List<ProductSku> list(int pid);

    /**
     * 添加数据到产品SKU
     *
     * @param list 添加对象
     */
    @Insert({"<script>REPLACE INTO eb_product_sku(pid, combo, sn, stock, retail, market, purchase) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "(#{item.pid}, #{item.combo}, #{item.sn}, #{item.stock}, #{item.retail}, #{item.market}, #{item.purchase})",
            "</foreach></script>"})
    void save(List<ProductSku> list);

    /**
     * 从产品SKU删除数据
     *
     * @param pid 商品ID
     */
    @Delete("DELETE FROM eb_product_sku WHERE pid = #{pid}")
    void remove(int pid);
}
