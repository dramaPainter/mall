package drama.painter.web.rbac.model.oa;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class Operation {
    String timestamp;
    String project;
    String timespan;
    String username;
    String session;
    String ip;
    String url;
    String parameter;
    String result;
}