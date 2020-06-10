package drama.painter.core.web.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author murphy
 */
@Component
public class Caches {
    static StringRedisTemplate redis;
    static HashOperations<String, String, String> hash;

    @Autowired
    public void init(StringRedisTemplate redis) {
        Caches.redis = redis;
    }

    /**
     * 查找缓存
     *
     * @param key 要查找的键值
     * @return 特定的数据
     */
    public static <T> T get(String key, Class<T> clazz) {
        String o = redis.opsForValue().get(key);
        return Json.parseObject(o, clazz);
    }

    /**
     * 查找缓存
     *
     * @param key 要查找的键值
     * @return 特定的数据
     */
    public static <T> T get(String key, TypeReference<T> type) {
        String o = redis.opsForValue().get(key);
        return Json.parseObject(o, type);
    }

    /**
     * 查找缓存
     *
     * @param key 要查找的键值
     * @return 特定的数据
     */
    public static <T> T getHash(String key, String field, Class<T> clazz) {
        Object o = redis.opsForHash().get(key, field);
        return o == null ? null : Json.parseObject(o.toString(), clazz);
    }

    /**
     * 查找缓存
     *
     * @param key 要查找的键值
     * @return 特定的数据
     */
    public static <T> List<T> getHash(String key, List<String> fields, TypeReference<List<T>> type, Function<String, T> lost) {
        HashOperations<String, String, String> hash = redis.opsForHash();
        List<String> hit = hash.multiGet(key, fields);
        List<T> list = hit == null ? Collections.EMPTY_LIST : Json.parseObject(hit.toString(), type);
        for (int i = 0; i< hit.size(); i++) {
            if( Objects.isNull(hit.get(i))) {
                list.set(i, lost.apply(fields.get(i)));
            }
        }
        fields.clear();
        hit.clear();
        return list;
    }

    /**
     * 添加缓存
     *
     * @param key    缓存的键值
     * @param data   缓存的数据
     * @param expire 缓存的时间，单位：秒(-1表示不限制)
     */
    public static void set(String key, Object data, int expire) {
        redis.opsForValue().set(key, Json.toJsonString(data));
        if (expire == -1) {
            redis.persist(key);
        } else {
            long millis = Math.round((expire + Math.random()) * 1000);
            redis.expire(key, millis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 添加缓存
     *
     * @param key    缓存的键值
     * @param data   缓存的数据
     * @param expire 缓存的时间，单位：秒(-1表示不限制)
     */
    public static void setHash(String key, String field, Object data, int expire) {
        redis.opsForHash().put(key, field, Json.toJsonString(data));
        if (expire == -1) {
            redis.persist(key);
        } else {
            long millis = Math.round((expire + Math.random()) * 1000);
            redis.expire(key, millis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 删除缓存
     *
     * @param key 缓存的键值
     */
    public static void remove(String key) {
        redis.delete(key);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存的键值
     */
    public static void removeHash(String key, String field) {
        redis.opsForHash().delete(key, field);
    }

    public static void lock() {
        //redis.setEnableTransactionSupport();
    }
}
