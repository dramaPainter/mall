package drama.painter.web.rbac.service.intf.com;

import drama.painter.core.web.enums.UploadEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.com.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author murphy
 */
public interface IImage {
    Result<List<Image>> list(int page, UploadEnum type, String value);

    Result save(Object file, UploadEnum type, String value);

    Result saveList(List<MultipartFile> files, UploadEnum type, String value);

    Result update(List<Image> list);

    void remove(int id);
}
