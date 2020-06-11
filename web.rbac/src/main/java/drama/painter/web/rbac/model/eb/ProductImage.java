package drama.painter.web.rbac.model.eb;

import drama.painter.core.web.enums.UploadEnum;
import lombok.Data;

/**
 * @author murphy
 */
@Data
public class ProductImage {
    Integer id;
    UploadEnum type;
    Integer value;
    String url;
}
