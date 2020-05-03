package drama.painter.core.web.dal;

import drama.painter.core.web.enums.BaseEnum;
import drama.painter.core.web.misc.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * 打印SQL日志
 *
 * @author murphy
 */
@Slf4j(topic = "sql")
@Intercepts({
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
class PageInterceptor implements Interceptor {
    static final DefaultReflectorFactory REFLECTOR = new DefaultReflectorFactory();
    static final ObjectFactory DEFAULT = SystemMetaObject.DEFAULT_OBJECT_FACTORY;
    static final ObjectWrapperFactory WRAPPER = SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

    static final String PAGE_OBJECT = "page";
    static final String SQL_LOG = "[分页]{}ms [查询]{}ms [语句]{}";
    static final Map<String, ISqlLog> HANDLER = new HashMap(3);

    static final ThreadLocal<Long> PAGING = new ThreadLocal();

    static {
        HANDLER.put("prepare", new PrepareHandler());
        HANDLER.put("query", new QueryHandler());
        HANDLER.put("update", new QueryHandler());
    }

    static void recordSql(Invocation invocation, long time) {
        long paging = PAGING.get();
        PAGING.remove();
        if (log.isDebugEnabled()) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaObject = MetaObject.forObject(statementHandler, DEFAULT, WRAPPER, REFLECTOR);
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            String sql = getSqlText(mappedStatement.getConfiguration(), statementHandler.getBoundSql());
            log.debug(SQL_LOG, paging, System.currentTimeMillis() - time, sql);
        }
    }

    static String getSqlText(Configuration config, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = config.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = parseSql(sql, parameterObject);
            } else {
                MetaObject metaObject = config.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        sql = parseSql(sql, metaObject.getValue(propertyName));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        sql = parseSql(sql, boundSql.getAdditionalParameter(propertyName));
                    } else {
                        sql = sql.replaceFirst("\\?", "<缺失>");
                    }
                }
            }
        }
        return sql;
    }

    static String parseSql(String sql, Object obj) {
        if (obj instanceof String || obj instanceof Timestamp || obj instanceof Date) {
            sql = sql.replaceFirst("\\?", String.join("", "'", ((String) obj).replaceAll("'", "''"), "'"));
        } else if (obj instanceof BaseEnum) {
            BaseEnum base = (BaseEnum) obj;
            sql = sql.replaceFirst("\\?", String.join("", String.valueOf(base.getValue()), " /* ", base.getName(), " */ "));
        } else {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(obj == null ? "NULL" : obj.toString()));
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return HANDLER.get(invocation.getMethod().getName()).proceed(invocation);
    }

    interface ISqlLog {
        /**
         * 日志操作方法
         *
         * @param invocation
         * @return
         * @throws SQLException
         */
        Object proceed(Invocation invocation) throws SQLException;
    }

    static class PrepareHandler implements ISqlLog {
        @Override
        public Object proceed(Invocation invocation) throws SQLException {
            long now = System.currentTimeMillis();
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaObject = MetaObject.forObject(statementHandler, DEFAULT, WRAPPER, REFLECTOR);
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

            if (mappedStatement.getSqlCommandType() == SqlCommandType.SELECT) {
                BoundSql boundSql = statementHandler.getBoundSql();
                String sql = boundSql.getSql();
                Map<?, ?> parameter = (Map<?, ?>) boundSql.getParameterObject();
                if (parameter != null && parameter.containsKey(PAGE_OBJECT)) {
                    String countSql = "SELECT COUNT(1) FROM (" + sql + ") pagination";
                    Connection connection = (Connection) invocation.getArgs()[0];
                    PreparedStatement countStatement = connection.prepareStatement(countSql);
                    ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
                    parameterHandler.setParameters(countStatement);
                    ResultSet rs = countStatement.executeQuery();
                    Page page = (Page) parameter.get(PAGE_OBJECT);

                    if (rs.next()) {
                        page.setRowCount(rs.getInt(1));
                    }

                    rs.close();
                    countStatement.close();

                    String pageSql = sql + " LIMIT " + page.getOffset() + ", " + page.getSize();
                    metaObject.setValue("delegate.boundSql.sql", pageSql);
                }
            }

            try {
                Object value = invocation.proceed();
                PAGING.set(System.currentTimeMillis() - now);
                return value;
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
    }

    static class QueryHandler implements ISqlLog {
        @Override
        public Object proceed(Invocation invocation) {
            long now = System.currentTimeMillis();
            try {
                return invocation.proceed();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                recordSql(invocation, now);
            }
        }
    }
}
