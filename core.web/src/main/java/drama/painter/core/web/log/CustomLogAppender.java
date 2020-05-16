package drama.painter.core.web.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import drama.painter.core.web.log.appender.ApiLogAppender;
import drama.painter.core.web.log.fmt.*;
import drama.painter.core.web.utility.ClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
public class CustomLogAppender extends AppenderBase<ILoggingEvent> {
    private LogFormat format;
    static final Map<LogFormat, IAppender> MAP = new HashMap();

    static {
        String path = ApiLogAppender.class.getName();
        path = path.substring(0, path.lastIndexOf("."));
        Set<Class<?>> list = ClassLoader.getClassInstance(path);
        try {
            for (Class<?> pay : list) {
                IAppender service = (IAppender) pay.newInstance();
                MAP.put(service.getFormat(), service);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        list.clear();
    }

    public LogFormat getFormat() {
        return format;
    }

    public void setFormat(LogFormat format) {
        this.format = format;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (Objects.isNull(ClientInstance.client) || ClientInstance.client instanceof NoopClient) {
            logFile(event);
        } else {
            IAppender appender = MAP.get(format);
            appender = appender == null ? MAP.get(LogFormat.API) : appender;
            LogEntity entity = appender.format(event);
            ClientInstance.client.log(entity);
        }
    }

    private void logFile(ILoggingEvent event) {
        String name = MAP.keySet().stream()
                .filter(o -> o.name().toLowerCase().equals(event.getLoggerName()))
                .findAny()
                .orElse(LogFormat.API).name();

        Logger log = LoggerFactory.getLogger(name.toLowerCase());
        if (event.getLevel() == Level.DEBUG) {
            log.debug(event.getFormattedMessage());
        } else if (event.getLevel() == Level.INFO) {
            log.info(event.getFormattedMessage());
        } else if (event.getLevel() == Level.WARN) {
            log.warn(event.getFormattedMessage());
        } else if (event.getLevel() == Level.ERROR) {
            log.error(event.getFormattedMessage());
        } else if (event.getLevel() == Level.TRACE) {
            log.trace(event.getFormattedMessage());
        } else if (event.getLevel() == Level.ALL) {
            log.debug(event.getFormattedMessage());
        }
    }
}
