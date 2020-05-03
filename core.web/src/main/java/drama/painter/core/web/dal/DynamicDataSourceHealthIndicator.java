package drama.painter.core.web.dal;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 把数据库监控放到 Spring Boot Actuator 检查
 *
 * @author murphy
 */
@Component("dbHealthIndicator")
class DynamicDataSourceHealthIndicator extends DataSourceHealthIndicator implements InitializingBean {
    final DataSource mySource;

    public DynamicDataSourceHealthIndicator(DataSource mySource) {
        this.mySource = mySource;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        setDataSource(mySource);
        super.doHealthCheck(builder);
    }

    @Override
    public void afterPropertiesSet() {
    }
}
