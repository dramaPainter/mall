package drama.painter.core.web.log.fmt;

import org.springframework.stereotype.Component;

/**
 * @author murphy
 */
@Component
public class NoopClient implements IClient {
    @Override
    public void log(LogEntity entity) {
    }
}
