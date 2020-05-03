package drama.painter.core.web.dal;

import com.zaxxer.hikari.HikariDataSource;
import drama.painter.core.web.utility.Encrypts;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 注册数据库源
 *
 * @author murphy
 */
@Component
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    static final String COMMA = ",";
    static final String DATASOURCE_TYPE_DEFAULT = HikariDataSource.class.getName();
    static final String POOL_NAME = "pool-name";
    static final String DRIVER = "driver";
    static final String READ_ONLY = "read-only";
    final Map<String, DataSource> dataSourceMap = new HashMap<>();
    DataSource defaultDataSource;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        initDataSource(environment);
        initDefaultDataSource(environment);
    }

    void initDefaultDataSource(Environment env) {
        defaultDataSource = dataSourceMap.get("master");
    }

    void initDataSource(Environment env) {
        // 读取配置文件获取更多数据源
        String prefixes = env.getProperty("database.names");
        if (Objects.nonNull(prefixes)) {
            for (String prefix : prefixes.split(COMMA)) {
                // 多个数据源
                Map<String, String> map = new HashMap<>(8);
                map.put("driver", env.getProperty("database." + prefix + ".driver"));
                map.put("url", env.getProperty("database." + prefix + ".url"));
                map.put("username", env.getProperty("database." + prefix + ".username"));
                map.put("password", Encrypts.decrypt(env.getProperty("database." + prefix + ".password")));
                map.put("type", env.getProperty("database." + prefix + ".type"));
                map.put("pool-name", env.getProperty("database." + prefix + ".pool-name"));
                map.put("read-only", env.getProperty("database." + prefix + ".read-only"));
                map.put("max-pool-size", env.getProperty("database." + prefix + ".max-pool-size"));
                DataSource ds = buildDataSource(map);
                map.clear();
                dataSourceMap.put(prefix, ds);
            }
        }
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata meta, BeanDefinitionRegistry registry) {

        Map<Object, Object> ds = new HashMap<>(8);
        //添加默认数据源
        ds.put("dataSource", this.defaultDataSource);
        DynamicDataSourceContextHolder.DATASOURCE.add("dataSource");
        //添加其他数据源
        ds.putAll(dataSourceMap);
        DynamicDataSourceContextHolder.DATASOURCE.addAll(dataSourceMap.keySet());

        GenericBeanDefinition bean = new GenericBeanDefinition();
        bean.setBeanClass(DynamicDataSource.class);
        bean.setSynthetic(true);
        MutablePropertyValues mpv = bean.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", ds);
        registry.registerBeanDefinition("dataSource", bean);
    }

    public DataSource buildDataSource(Map<String, String> map) {
        Object type = map.get("type");
        if (type == null) {
            type = DATASOURCE_TYPE_DEFAULT;
        }

        if (type.equals(DATASOURCE_TYPE_DEFAULT)) {
            HikariDataSource ds = new HikariDataSource();
            ds.addDataSourceProperty("cachePrepStmts", true);
            ds.addDataSourceProperty("prepStmtCacheSize", 250);
            ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            ds.addDataSourceProperty("useServerPrepStmts", true);
            ds.addDataSourceProperty("useLocalSessionState", true);
            ds.addDataSourceProperty("rewriteBatchedStatements", true);
            ds.addDataSourceProperty("cacheResultSetMetadata", true);
            ds.addDataSourceProperty("cacheServerConfiguration", true);
            ds.addDataSourceProperty("elideSetAutoCommits", true);
            ds.addDataSourceProperty("maintainTimeStats", false);
            ds.addDataSourceProperty("maintainTimeStats", false);

            ds.addDataSourceProperty("useUnicode", true);
            ds.addDataSourceProperty("useSSL", false);
            ds.addDataSourceProperty("characterEncoding", "utf8");
            ds.addDataSourceProperty("socketTimeout", 120 * 1000);
            ds.addDataSourceProperty("connectTimeout", 10 * 1000);

            int poolSize = map.get("max-pool-size") == null ? 10 : Integer.parseInt(map.get("max-pool-size"));
            ds.setLeakDetectionThreshold(6 * 1000);
            ds.setMaximumPoolSize(poolSize);
            ds.setMaxLifetime(90 * 1000);
            ds.setIdleTimeout(10 * 1000);
            ds.setMinimumIdle(2);
            ds.setConnectionTimeout(10 * 1000);
            ds.setJdbcUrl(map.get("url"));
            ds.setUsername(map.get("username"));
            ds.setPassword(map.get("password"));

            if (map.get(POOL_NAME) != null) {
                ds.setPoolName(map.get(POOL_NAME));
            } else if (map.get(DRIVER) != null) {
                ds.setDriverClassName(map.get(DRIVER));
            } else if (map.get(READ_ONLY) != null) {
                ds.setReadOnly("true".equals(map.get(READ_ONLY)));
            }
            return ds;
        } else {
            throw new RuntimeException("暂不支持mysql以外的数据库。");
        }
    }
}
