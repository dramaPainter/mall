package drama.painter.core.web.dal;

import drama.painter.core.web.enums.BaseEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @author murphy
 */
@Configuration
public class Mybatis {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        SqlSessionFactory sqlSessionFactory = factory.getObject();
        org.apache.ibatis.session.Configuration config = sqlSessionFactory.getConfiguration();
        config.setMapUnderscoreToCamelCase(true);
        config.setDefaultEnumTypeHandler(EnumHandler.class);
        config.addInterceptor(new PageInterceptor());
        return sqlSessionFactory;
    }

    public static class EnumHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
        BaseTypeHandler typeHandler;

        public EnumHandler(Class<E> type) {
            if (type == null) {
                throw new IllegalArgumentException("Type argument cannot be null");
            } else if (BaseEnum.class.isAssignableFrom(type)) {
                typeHandler = new EnumParser(type);
            } else {
                typeHandler = new EnumTypeHandler(type);
            }
        }

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
            typeHandler.setNonNullParameter(ps, i, parameter, jdbcType);
        }

        @Override
        public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return (E) typeHandler.getNullableResult(rs, columnName);
        }

        @Override
        public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return (E) typeHandler.getNullableResult(rs, columnIndex);
        }

        @Override
        public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return (E) typeHandler.getNullableResult(cs, columnIndex);
        }
    }

    public static class EnumParser<E extends Enum<?> & BaseEnum> extends BaseTypeHandler<BaseEnum> {
        final List<E> map;

        public EnumParser(Class<E> type) {
            if (type == null) {
                throw new IllegalArgumentException("枚举类型不能为空。");
            }
            map = Arrays.asList(type.getEnumConstants());
        }

        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum parameter, JdbcType jdbcType) throws SQLException {
            ps.setInt(i, parameter.getValue());
        }

        @Override
        public BaseEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
            String code = rs.getString(columnName);
            return rs.wasNull() ? null : map.stream().filter(o -> String.valueOf(o.getValue()).equals(code)).findAny().orElse(null);
        }

        @Override
        public BaseEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            String code = rs.getString(columnIndex);
            return rs.wasNull() ? null : map.stream().filter(o -> String.valueOf(o.getValue()).equals(code)).findAny().orElse(null);
        }

        @Override
        public BaseEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            String code = cs.getString(columnIndex);
            return cs.wasNull() ? null : map.stream().filter(o -> String.valueOf(o.getValue()).equals(code)).findAny().orElse(null);
        }
    }
}

