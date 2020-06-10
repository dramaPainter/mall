package drama.painter.web.rbac.controller.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.eb.Product;
import drama.painter.web.rbac.service.intf.eb.IProduct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class ProductController {
    final IProduct product;

    public ProductController(IProduct product) {
        this.product = product;
    }

    @GetMapping("/eb/product")
    public Result<List<Product>> product(int page, StatusEnum status, SearchEnum key, String value) {
        return product.list(page, status, key, value);
    }

    @PostMapping("/eb/product/save")
    public Result product_save(@RequestBody Product p) {
        product.save(p);
        return Result.SUCCESS;
    }

    @PostMapping("/eb/product/save/sku")
    public Result product_sku(@RequestBody Product p) {
        product.saveSku(p);
        return Result.SUCCESS;
    }

    @PostMapping("/eb/product/display")
    public Result product_display(@RequestBody String code) {
        product.display(code);
        return Result.SUCCESS;
    }
}
