package drama.painter.core.web.dal;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 动态切换数据库
 *
 * @author murphy
 */
@Slf4j
@Aspect
@Order(-5)
@Component
class DynamicDataSourceAspect {
    /**
     * 注意：只能注解到方法上，不能注解到interface上。
     */
    @Before("@annotation(TargetDataSource)")
    public void changeDataSource(JoinPoint point) {
        String database = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(TargetDataSource.class).value();
        if (DynamicDataSourceContextHolder.contains(database)) {
            log.info("使用数据源：{} - {}", database, point.toString());
            DynamicDataSourceContextHolder.setDataSourceType(database);
        } else {
            log.error("数据源 {} 不存在。使用默认的数据源 -> {}", database, point.getSignature());
        }
    }

    @After("@annotation(TargetDataSource)")
    public void clearDataSource(JoinPoint point) {
        String database = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(TargetDataSource.class).value();
        if (DynamicDataSourceContextHolder.contains(database)) {
            log.info("清除数据源：{} - {}", database, point.toString());
            DynamicDataSourceContextHolder.clearDataSourceType();
        } else {
            log.error("数据源 {} 不存在。默认的数据源 -> {}", database, point.getSignature());
        }
    }
}
