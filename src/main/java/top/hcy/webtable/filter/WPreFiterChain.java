package top.hcy.webtable.filter;

import top.hcy.webtable.common.WebTableContext;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.filter
 * @ClassName: WFiterChain
 * @Author: hcy
 * @Description: webtable filter chain
 * @Date: 2020/1/15 11:05
 * @Version: 1.0
 */
public class WPreFiterChain implements WFilterChain,WHandlerFilter {
    private  WHandlerFilter[]  filters;
    ThreadLocal<Integer> point;


    @Override
    public void doFilter(WebTableContext ctx) {
        int len = filters.length;
        for (int i = 0; i < len; i++) {
            filters[i].doFilter(ctx);
            if (ctx.isError()){
                return;
            }
        }
    }
}
