package drama.painter.web.rbac.service.intf.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.eb.Category;

import java.util.List;

/**
 * @author murphy
 */
public interface ICategory {
    Result<List<Category>> list(int page, StatusEnum status, SearchEnum key, String value);
}
