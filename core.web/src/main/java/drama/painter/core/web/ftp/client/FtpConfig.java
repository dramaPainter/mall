package drama.painter.core.web.ftp.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author murphy
 */
@Slf4j
@Component
public class FtpConfig {
    private final ObjectPool<FTPClient> pool;

    @Autowired
    public FtpConfig(FtpConfigProperties props) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setSoftMinEvictableIdleTimeMillis(50000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        pool = new GenericObjectPool<>(new FtpClient(props), poolConfig);
        preLoadingFtpClient(props.getInitialSize(), poolConfig.getMaxIdle());

        FtpPool.init(pool);
    }

    private void preLoadingFtpClient(Integer initialSize, int maxIdle) {
        if (initialSize == null || initialSize <= 0) {
            return;
        }

        int size = Math.min(initialSize, maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (pool != null) {
            pool.close();
            log.info("已销毁ftpClient进程池。");
        }
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "ftp")
    public static class FtpConfigProperties {
        private String host = "127.0.0.1";
        private int port;
        private String username;
        private String password;
        private String basePath;
        private String domain;
        private int bufferSize;
        private int initialSize;
        private int connectTimeOut;
        private int readTimeOut;
        private boolean localized;
    }
}
