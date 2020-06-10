package drama.painter.web.rbac.service.impl.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.eb.Category;
import drama.painter.web.rbac.service.intf.eb.ICategory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
@Service
public class CategoryImpl implements ICategory {
    @Override
    public Result<List<Category>> list(int page, StatusEnum status, SearchEnum key, String value) {
        Page p = new Page(page, Constant.PAGE_SIZE);
        List<Category> list = Arrays.asList(new Category(1, "默认分类", 0, StatusEnum.ENABLE));
        return Result.toData(p.getRowCount(), list);
    }
}
