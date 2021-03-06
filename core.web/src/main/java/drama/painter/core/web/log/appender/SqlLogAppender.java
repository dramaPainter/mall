package drama.painter.core.web.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import drama.painter.core.web.log.fmt.IAppender;
import drama.painter.core.web.log.fmt.LogEntity;
import drama.painter.core.web.log.fmt.LogFormat;
import drama.painter.core.web.utility.Dates;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 */
public class SqlLogAppender implements IAppender {
    @Override
    public LogFormat getFormat() {
        return LogFormat.SQL;
    }

    @Override
    public LogEntity format(ILoggingEvent event) {
        Object[] args = event.getArgumentArray();
        String timestamp = Dates.toDateTimeMillis();
        String index = "sql-" + timestamp.substring(0, 7).replaceFirst("-", "");

        Map<String, Object> map = new HashMap();
        map.put("timestamp", timestamp);
        map.put("project", args[0]);
        map.put("countTime", args[1]);
        map.put("queryTime", args[2]);
        map.put("sql", args[3]);

        return new LogEntity(index, null, map);
    }
}
