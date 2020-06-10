package drama.painter.core.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author murphy
 */
@Configuration
@EnableCaching(order = Ordered.HIGHEST_PRECEDENCE)
public class Redis extends CachingConfigurerSupport {
    private final RedisConfig redisModel;

    public Redis(RedisConfig redisModel) {
        this.redisModel = redisModel;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration rsa = new RedisStandaloneConfiguration();
        rsa.setHostName(redisModel.getHost());
        rsa.setDatabase(redisModel.getDatabase());
        rsa.setPassword(RedisPassword.of(redisModel.getPassword()));
        rsa.setPort(redisModel.getPort());
        return new JedisConnectionFactory(rsa);
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "redis")
    public static class RedisConfig {
        private String host;
        private int port;
        private String password;
        private int database;
        private int expire;
    }
}
