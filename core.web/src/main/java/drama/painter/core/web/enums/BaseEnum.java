package drama.painter.core.web.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author murphy
 */
public interface BaseEnum {
    /**
     * 枚举的ID值
     *
     * @return
     */
    @JsonValue
    int getValue();

    /**
     * 枚举的中文名
     *
     * @return
     */
    String getName();
}
