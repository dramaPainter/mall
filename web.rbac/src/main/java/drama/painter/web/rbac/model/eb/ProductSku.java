package drama.painter.web.rbac.model.eb;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author murphy
 */
@Data
public class ProductSku {
    Integer pid;
    String combo;
    Long sn;
    Integer stock;
    BigDecimal retail;
    BigDecimal market;
    BigDecimal purchase;
}
