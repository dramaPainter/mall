package drama.painter.web.rbac.service.impl.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.eb.Brand;
import drama.painter.web.rbac.service.intf.eb.IBrand;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
@Service
public class BrandImpl implements IBrand {
    @Override
    public Result<List<Brand>> list(int page,  StatusEnum status, SearchEnum key, String value) {
        Page p = new Page(page, Constant.PAGE_SIZE);
        List<Brand> list = Arrays.asList(new Brand(1, "默认品牌", 0, StatusEnum.ENABLE));
        return Result.toData(p.getRowCount(), list);
    }
}
