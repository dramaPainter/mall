package drama.painter.web.rbac.service.intf.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.eb.Product;

import java.util.List;

/**
 * @author murphy
 */
public interface IProduct {
    Result<List<Product>> list(int page, StatusEnum status, SearchEnum key, String value);

    Product get(String code);

    void save(Product p);

    void saveSku(Product p);

    void display(String code);
}
