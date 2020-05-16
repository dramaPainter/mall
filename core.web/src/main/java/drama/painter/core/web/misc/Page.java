package drama.painter.core.web.misc;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class Page {
    int page;
    int offset;
    int size;
    int rowCount;

    public Page(int page, int size) {
        this.page = page;
        this.offset = page <= 0 ? 0 : (page - 1) * (size <= 0 ? Constant.PAGE_SIZE : size);
        this.size = size;
    }

    public Page(int page) {
        this(page, Constant.PAGE_SIZE);
    }
}
