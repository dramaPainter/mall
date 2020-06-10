package drama.painter.web.rbac.controller.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.eb.Brand;
import drama.painter.web.rbac.service.intf.eb.IBrand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class BrandController {
    final IBrand brand;

    public BrandController(IBrand brand) {
        this.brand = brand;
    }

    @GetMapping("/eb/brand")
    public Result<List<Brand>> brand(int page, StatusEnum status, SearchEnum key, String value) {
        return brand.list(page, status, key, value);
    }
}
