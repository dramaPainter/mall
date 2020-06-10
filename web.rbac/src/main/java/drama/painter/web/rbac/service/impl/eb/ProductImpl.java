package drama.painter.web.rbac.service.impl.eb;

import drama.painter.core.web.enums.SearchEnum;
import drama.painter.core.web.enums.StatusEnum;
import drama.painter.core.web.ftp.upload.IUpload;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Caches;
import drama.painter.core.web.utility.Randoms;
import drama.painter.web.rbac.mapper.eb.ProductMapper;
import drama.painter.web.rbac.model.eb.Product;
import drama.painter.web.rbac.service.intf.eb.IProduct;
import drama.painter.web.rbac.service.intf.eb.IProductSku;
import drama.painter.web.rbac.tool.AssertEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static drama.painter.web.rbac.service.intf.other.ICache.PRODUCT_REFERENCE;

/**
 * @author murphy
 */
@Service
public class ProductImpl implements IProduct {
    final ProductMapper productMapper;
    final IProductSku sku;
    final IUpload upload;

    public ProductImpl(ProductMapper productMapper, IUpload u, IProductSku sku) {
        this.productMapper = productMapper;
        this.upload = u;
        this.sku = sku;
    }

    @Override
    public Product get(String code) {
        Product product = Caches.getHash("Product", code, Product.class);
        if (product == null) {
            product = productMapper.get(code);
            Caches.setHash("Product", code, product, -1);
        }
        return product;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<List<Product>> list(int page, StatusEnum status, SearchEnum key, String value) {
        Page p = new Page(page, Constant.PAGE_SIZE);
        List<String> ids = productMapper.list(p, status, key, value);
        List<Product> list = Caches.getHash("Product", ids, PRODUCT_REFERENCE, this::get);
        return Result.toData(p.getRowCount(), list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Product p) {
        if (Objects.isNull(p.getId())) {
            p.setCode(Randoms.getNonceString());
            productMapper.add(p);
        } else {
            productMapper.update(p);
        }
        Caches.removeHash("Product", p.getCode());
    }

    @Override
    public void saveSku(Product p) {
        sku.saveSku(p);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void display(String code) {
        Product p = get(code);
        AssertEnum.NOT_FOUND.doAssert(p == null, "商品:" + code);
        StatusEnum status = p.getStatus() == StatusEnum.ENABLE ? StatusEnum.DISABLE : StatusEnum.ENABLE;
        productMapper.display(p.getId(), status);
        Caches.removeHash("Product", p.getCode());
    }
}
