package drama.painter.core.web.log.fmt;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author murphy
 */
public interface IAppender {
    LogFormat getFormat();

    LogEntity format(ILoggingEvent event);
}
