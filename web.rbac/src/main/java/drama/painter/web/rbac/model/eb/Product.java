package drama.painter.web.rbac.model.eb;

import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.enums.YesNoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author murphy
 */
@NoArgsConstructor
@Data
public class Product {
    public Product(Integer id, StatusEnum status) {
        this.id = id;
        this.status = status;
    }

    Integer id;
    String name;
    String code;
    Integer category;
    Integer brand;
    Integer sort;
    StatusEnum status;
    YesNoEnum hottest;
    YesNoEnum latest;
    Integer sale;
    String keyword;
    String avatar;
    String body;

    List<ProductSku> sku;
    List<ProductSkuName> spu;
}
