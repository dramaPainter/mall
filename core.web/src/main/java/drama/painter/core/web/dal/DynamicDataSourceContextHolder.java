package drama.painter.core.web.dal;

import java.util.ArrayList;
import java.util.List;

/**
 * 每个线程只指定一个数据库源
 *
 * @author murphy
 */
public class DynamicDataSourceContextHolder {
    protected static final List<String> DATASOURCE = new ArrayList<>();
    static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    protected static String getDataSourceType() {
        return LOCAL.get();
    }

    public static void setDataSourceType(String dataSourceType) {
        LOCAL.set(dataSourceType);
    }

    public static void clearDataSourceType() {
        LOCAL.remove();
    }

    public static boolean contains(String dataSourceId) {
        return DATASOURCE.contains(dataSourceId);
    }
}
