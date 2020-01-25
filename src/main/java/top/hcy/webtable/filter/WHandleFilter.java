package top.hcy.webtable.filter;

import top.hcy.webtable.common.WebTableContext;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.filter
 * @ClassName: Handler Filter
 * @Author: hcy
 * @Description: webtable filter interface
 * @Date: 2020/1/15 11:02
 * @Version: 1.0
 */
public interface WHandleFilter {
    void doFilter(WebTableContext ctx);
}
