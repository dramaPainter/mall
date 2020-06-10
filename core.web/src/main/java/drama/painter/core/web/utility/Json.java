package drama.painter.core.web.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author murphy
 */
@Component
public final class Json {
    static ObjectMapper MAPPER = new ObjectMapper();
    static final TypeReference<Map<String, Object>> MAP_REFERENCE;

    static {
        MAP_REFERENCE = new TypeReference<Map<String, Object>>() {};
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：fromJSON(json, Student.class)
     * (2)转换为数组,如Student[],将第二个参数传递为Student[].class
     */
    public static <T> T parseObject(String json, Class<T> valueType) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
           return MAPPER.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的内部泛形成员对象。
     * 如Result&lt;List&lt;Result>>,将第二个参数传递为
     * new TypeReference&lt;Result&lt;List&lt;Result>>>(){}
     */
    public static <T> T parseObject(String json, TypeReference<T> reference) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, reference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：fromJSON(json, Student.class)
     * (2)转换为数组,如Student[],将第二个参数传递为Student[].class
     */
    public static <T> T parseByteObject(byte[] json, Class<T> valueType) {
        if (Objects.isNull(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的内部泛形成员对象。
     * 如Result&lt;List&lt;Result>>,将第二个参数传递为
     * new TypeReference&lt;Result&lt;List&lt;Result>>>(){}
     */
    public static <T> T parseByteObject(byte[] json, TypeReference<T> reference) {
        if (Objects.isNull(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, reference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> toMap(Object obj) {
        if (Objects.isNull(obj)) {
            return Collections.EMPTY_MAP;
        }

        try {
            return MAPPER.convertValue(obj, MAP_REFERENCE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
