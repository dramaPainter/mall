package drama.painter.core.web.utility;

import lombok.Data;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author murphy
 */
public class Caches {
    static final Map<String, CacheData> MAP = new ConcurrentHashMap<>();

    static {
        new Timer("CacheExpireRemoveThread").schedule(new CacheExpireRemoveThread(), 0, 60 * 1000);
    }

    /**
     * 查找缓存
     *
     * @param key 要查找的键值
     * @param <T> 任意数据类型
     * @return 特定的数据
     */
    public static <T> T get(String key) {
        CacheData<T> data = MAP.get(key);
        return data == null ? null : data.getData();
    }

    /**
     * 添加缓存
     *
     * @param key    缓存的键值
     * @param data   缓存的数据
     * @param expire 缓存的时间，单位：秒(-1表示不限制)
     * @param <T>    任意数据类型
     */
    public static <T> void add(String key, T data, int expire) {
        MAP.put(key, new CacheData(data, expire));
    }

    /**
     * 删除缓存
     *
     * @param key 缓存的键值
     */
    public static void remove(String key) {
        MAP.remove(key);
    }

    static class CacheExpireRemoveThread extends TimerTask {
        @Override
        public void run() {
            for (Map.Entry<String, CacheData> entry : MAP.entrySet()) {
                if (entry.getValue().getExpire() == -1) {
                    continue;
                }
                if (entry.getValue().getSaveTime() + entry.getValue().getExpire() <= System.currentTimeMillis()) {
                    MAP.remove(entry.getKey());
                }
            }
        }
    }

    @Data
    static class CacheData<T> {
        T data;
        long saveTime;
        long expire;

        CacheData(T t, int expire) {
            this.data = t;
            this.expire = expire == -1 ? -1 : (expire <= 0 ? 0 : expire * 1000);
            this.saveTime = System.currentTimeMillis();
        }
    }
}
