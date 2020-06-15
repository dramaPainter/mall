package drama.painter.web.rbac.model.com;

import drama.painter.core.web.enums.UploadEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Image {
    Integer id;
    String name;
    UploadEnum type;
    Integer value;
    String url;
}
