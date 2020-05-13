package drama.painter.core.web.misc;

import drama.painter.core.web.enums.MenuTypeEnum;
import drama.painter.core.web.enums.StatusEnum;
import lombok.Data;

/**
 * @author murphy
 */
@Data
public class Permission {
    private Integer id;
    private String name;
    private String url;
    private Integer pid;
    private MenuTypeEnum type;
    private Byte sort;
    private StatusEnum status;
}
