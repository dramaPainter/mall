package drama.painter.web.rbac.model.eb;

import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.enums.YesNoEnum;
import drama.painter.web.rbac.model.com.Image;
import lombok.Data;

import java.util.List;

/**
 * @author murphy
 */
@Data
public class Product {
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
    List<Image> image;
}
