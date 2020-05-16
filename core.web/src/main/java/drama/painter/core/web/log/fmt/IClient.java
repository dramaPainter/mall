package drama.painter.core.web.log.fmt;

/**
 * @author murphy
 */
@FunctionalInterface
public interface IClient {
    void log(LogEntity entity);
}
