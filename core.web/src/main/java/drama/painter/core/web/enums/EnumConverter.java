package drama.painter.core.web.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author murphy
 */
public class EnumConverter implements ConverterFactory<String, BaseEnum> {
    static final Map<Class, Converter> MAP = new WeakHashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> type) {
        Converter result = MAP.get(type);
        if (result == null) {
            result = new IntegerStrToEnum(type);
            MAP.put(type, result);
        }
        return result;
    }

    static class IntegerStrToEnum<T extends BaseEnum> implements Converter<String, T> {
        final List<T> map;

        public IntegerStrToEnum(Class<T> enumType) {
            map = Arrays.asList(enumType.getEnumConstants());
        }

        @Override
        public T convert(String source) {
            return map.stream()
                    .filter(o -> String.valueOf(o.getValue()).equals(source))
                    .findAny()
                    .orElse(null);
        }
    }
}
