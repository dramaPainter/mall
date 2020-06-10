package drama.painter.web.rbac.model.eb;

import drama.painter.core.web.enums.YesNoEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author murphy
 */
@Data
public class ProductSkuName {
    Integer id;
    Integer pid;
    String name;
    YesNoEnum impact;

    List<ProductSkuValue> list;
}
