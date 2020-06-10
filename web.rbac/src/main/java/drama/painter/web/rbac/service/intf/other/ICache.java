package drama.painter.web.rbac.service.intf.other;

import com.fasterxml.jackson.core.type.TypeReference;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.model.eb.Product;
import drama.painter.web.rbac.model.oa.Role;

import java.util.List;

/**
 * @author murphy
 */
public interface ICache {
    TypeReference<List<User>> STAFF_REFERENCE = new TypeReference<List<User>>() {};
    TypeReference<List<Role>> ROLE_REFERENCE = new TypeReference<List<Role>>() {};
    TypeReference<List<Permission>> PERMISSION_REFERENCE = new TypeReference<List<Permission>>() {};
    TypeReference<List<Product>> PRODUCT_REFERENCE = new TypeReference<List<Product>>() {};
}
