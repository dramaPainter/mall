package drama.painter.web.rbac.controller.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.eb.Category;
import drama.painter.web.rbac.service.intf.eb.ICategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class CategoryController {
    final ICategory category;

    public CategoryController(ICategory category) {
        this.category = category;
    }

    @GetMapping("/eb/category")
    public Result<List<Category>> category(int page, StatusEnum status, SearchEnum key, String value) {
        return category.list(page, status, key, value);
    }
}
