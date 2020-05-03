package drama.painter.core.web.misc;

import drama.painter.core.web.utility.Strings;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author murphy
 */
public class Constant {
    public static final byte PAGE_SIZE = 15;

    static final String NONE_PAGING_DATA = "<ul class='pager'><li style='line-height:30px'>暂无数据。</li></ul>";

    public static String toPage(int page, int recordCount, int size, boolean showTotal) {
        if (recordCount == 0) {
            return NONE_PAGING_DATA;
        }

        ServletRequestAttributes util = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String param = util.getRequest().getParameterMap().entrySet().stream()
                .filter(x -> !"page".equals(x.getKey().toLowerCase()))
                .map(p -> p.getKey() + "=" + Strings.urlencode(StringUtils.join(p.getValue())))
                .reduce((p1, p2) -> p1 + "&" + p2)
                .map(s -> "&" + s)
                .orElse("");

        int pageCount = (int) Math.ceil((double) recordCount / size);
        int startPage = page < 10 ? 1 : page / 10 * 10;
        int endPage = page < 10 ? (Math.min(startPage + 9, pageCount)) : (Math.min(startPage + 10, pageCount));
        endPage = Math.min(endPage, pageCount);
        String prevPageHref = page <= 1 ? "" : " href='?page=" + (page - 1) + param + "'";
        String nextPageHref = page >= pageCount ? "" : " href='?page=" + (page + 1) + param + "'";

        StringBuilder sb = new StringBuilder();
        sb.append("<ul class='pager'>").append("<li><a").append(prevPageHref).append(">上一页</a></li>");
        for (int i = startPage; i <= endPage; i++) {
            sb.append("<li><a").append(i == page ? "" : " href='?page=" + i + param + "'")
                    .append(i == page ? " class='current pnum'" : " class='pnum'")
                    .append(">").append(i).append("</a></li>");
        }

        sb.append("<li><a").append(nextPageHref).append(">下一页</a></li>");
        if (showTotal) {
            sb.append("<li class='hidden'></li>")
                    .append(String.format("<li class='space'><label>%d条记录</label><label class='farspace'>第</label></li>", recordCount))
                    .append(String.format("<li><input id='page' type='text' style='vertical-align:middle' value='%d' /></li>", page))
                    .append("<li><label class='ye'>页</label></li>")
                    .append(String.format("<li><a class='redirect' onclick=\"location.href='?page='+document.getElementById('page').value+'%s'\">转到</a></li>", param));
        }

        sb.append("</ul>");
        String result = sb.toString();
        sb.delete(0, sb.length());
        return result;
    }
}
