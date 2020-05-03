package drama.painter.core.web.dal;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 实现Bean查找单个数据源的功能类
 *
 * @author murphy
 */
class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
