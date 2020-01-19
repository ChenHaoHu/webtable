package top.hcy.webtable.filter;

import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.enums.WHandlerTypeCode;

import java.util.ArrayList;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.filter
 * @ClassName: WFiterChain
 * @Author: hcy
 * @Description: webtable filter chain
 * @Date: 2020/1/15 11:05
 * @Version: 1.0
 */
public class WFiterChainImpl extends AbstractFilterChain implements WHandlerFilter {
    private  ArrayList<WHandlerFilter> filters = new ArrayList<>();

    private WFiterChainImpl(){
        super();
    }

    public WFiterChainImpl(WHandlerTypeCode code) {
        super(code);
    }

    public boolean addFileterOnFist(WHandlerFilter f){
            filters.add(0,f);
            return true;
    }

    public boolean addFileterOnLast(WHandlerFilter f){
            filters.add(f);
            return true;
    }

    public boolean deleteFileter(WHandlerFilter f){
            filters.remove(f);
            return true;

    }


    @Override
    public void doFilter(WebTableContext ctx) {
        int len = filters.size();
        for (int i = 0; i < len; i++) {
            filters.get(i).doFilter(ctx);
            if (ctx.isError()){
                return ;
            }
        }
    }
}
