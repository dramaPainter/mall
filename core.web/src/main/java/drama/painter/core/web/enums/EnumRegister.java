package drama.painter.core.web.enums;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author murphy
 */
@Configuration
public class EnumRegister implements WebMvcConfigurer {
    public static final EnumConverter ENUM_CONVERTER = new EnumConverter();

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(ENUM_CONVERTER);
    }
}

