package drama.painter.web.rbac.service.intf.com;

import drama.painter.core.web.misc.Result;

import java.util.List;

/**
 * @author murphy
 */
public interface IImageSaver {
    void save(Result<String> r, String value);

    void saveList(Result<List<String>> r, String value);

    void remove(int id, String value);
}
