package top.hcy.webtable.filter;

import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.router.WHandlerType;

import java.util.ArrayList;



public class WFiterChainImpl extends AbstractFilterChain implements WHandleFilter {

    private WFiterChainImpl(){
        super();
    }

    public WFiterChainImpl(WHandlerType code) {
        super(code);
    }

    @Override
    public boolean addFitersOnFirst(AbstractFilterChain f) {
        ArrayList<WHandleFilter> filters = f.getFilters();
        this.filters.addAll(0,filters);
        return true;
    }

    @Override
    public boolean addFiterOnFirst(WHandleFilter f){
        filters.add(0,f);
        return true;
    }

    @Override
    public boolean addFiterOnLast(WHandleFilter f){
        filters.add(f);
        return true;
    }



    @Override
    public boolean deleteFiter(WHandleFilter f){
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
