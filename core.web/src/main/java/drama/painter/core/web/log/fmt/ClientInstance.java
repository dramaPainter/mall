package drama.painter.core.web.log.fmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author murphy
 */
@Component
public class ClientInstance {
    public static IClient client;

    @Autowired
    private void init(IClient autowiredClient) {
        client = autowiredClient;
    }
}
