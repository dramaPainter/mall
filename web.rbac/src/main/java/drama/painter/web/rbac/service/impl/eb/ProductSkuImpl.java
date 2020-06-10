package drama.painter.web.rbac.service.impl.eb;

import drama.painter.core.web.utility.Caches;
import drama.painter.web.rbac.mapper.eb.ProductSkuMapper;
import drama.painter.web.rbac.mapper.eb.ProductSkuNameMapper;
import drama.painter.web.rbac.mapper.eb.ProductSkuValueMapper;
import drama.painter.web.rbac.model.eb.Product;
import drama.painter.web.rbac.model.eb.ProductSkuName;
import drama.painter.web.rbac.model.eb.ProductSkuValue;
import drama.painter.web.rbac.service.intf.eb.IProductSku;
import drama.painter.web.rbac.tool.AssertEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Service
public class ProductSkuImpl implements IProductSku {
    final ProductSkuMapper skuMapper;
    final ProductSkuNameMapper skuNameMapper;
    final ProductSkuValueMapper skuValueMapper;

    public ProductSkuImpl(ProductSkuNameMapper name, ProductSkuValueMapper value, ProductSkuMapper sku) {
        this.skuNameMapper = name;
        this.skuValueMapper = value;
        this.skuMapper = sku;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSku(Product p) {
        AssertEnum.NOT_FOUND.doAssert(p.getName() == null, "参数:商品名");

        if (Objects.nonNull(p.getSku()) && p.getSku().size() > 0) {
            skuMapper.remove(p.getId());
            skuMapper.save(p.getSku());
        }

        if (Objects.nonNull(p.getSku()) && p.getSku().size() > 0) {
            List<ProductSkuName> updates = p.getSpu().stream().filter(o -> o.getId() != null).collect(Collectors.toList());
            if (!updates.isEmpty()) {
                skuNameMapper.removeNot(p.getId(), updates);
                skuNameMapper.update(updates);
            }

            List<ProductSkuName> adds = p.getSpu().stream().filter(o -> o.getId() == null).collect(Collectors.toList());
            if (!adds.isEmpty()) {
                skuNameMapper.add(adds);
            }

            p.getSpu().stream().forEach(o -> o.getList().stream()
                    .filter(t -> Objects.isNull(t.getNameId()))
                    .forEach(n -> n.setNameId(o.getId())));
            List<ProductSkuValue> list = p.getSpu().stream()
                    .map(ProductSkuName::getList)
                    .flatMap(u -> u.stream())
                    .collect(Collectors.toList());

            if (list.size() > 0) {
                skuValueMapper.removeNot(list);
                skuValueMapper.save(list);
            }

            updates.clear();
            adds.clear();
            list.clear();
        }

        Caches.removeHash("Product", p.getCode());
    }
}
