package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Dates;
import drama.painter.web.rbac.model.oa.Operation;
import drama.painter.web.rbac.service.oa.IOa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class OaController {
    final IOa oa;

    public OaController(IOa oa) {
        this.oa = oa;
    }

    @GetMapping("/oa/operation")
    public Result<List<Operation>> operation(int page, String startTime, String endTime, int timespan, String searchText) {
        startTime = Dates.modify(startTime, 0, Dates.DateTimeType.DATE_TIME_MILLIS, Dates.toDate() + " 00:00:00,000");
        endTime = Dates.modify(endTime, 0, Dates.DateTimeType.DATE_TIME_MILLIS, Dates.toDateTime() + ",000");
        return oa.listOperation(page, startTime, endTime, timespan, searchText);
    }
}
