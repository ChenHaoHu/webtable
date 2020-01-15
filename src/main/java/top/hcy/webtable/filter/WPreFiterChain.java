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
public class WPreFiterChain extends AbstractFilterChain implements WHandlerFilter {
    private  ArrayList<WHandlerFilter> filters = new ArrayList<>();
    private  ThreadLocal<Integer> point;
    private  ThreadLocal<Boolean> unLock  = new ThreadLocal<>();

    private  WPreFiterChain(){
        super();
    }

    public WPreFiterChain(WHandlerTypeCode code) {
        super(code);
        unLock.set(true);
    }


    public boolean addFileterOnFist(WHandlerFilter f){
        if (unLock.get()){
            filters.add(0,f);
            return true;
        }
        return false;
    }

    public boolean addFileterOnLast(WHandlerFilter f){
        if (unLock.get()){
            filters.add(f);
            return true;
        }
        return false;
    }

    public boolean deleteFileter(WHandlerFilter f){
        if (unLock.get()){
            filters.remove(f);
            return true;
        }
        return false;
    }


    @Override
    public void doFilter(WebTableContext ctx) {
        unLock.set(false);
        int len = filters.size();
        for (int i = 0; i < len; i++) {
            filters.get(i).doFilter(ctx);
            if (ctx.isError()){
                return;
            }
        }
    }

}
