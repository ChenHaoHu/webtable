package top.hcy.webtable.filter;

import lombok.Data;
import top.hcy.webtable.router.WHandlerType;

import java.util.ArrayList;


@Data
public abstract class AbstractFilterChain implements WFilterChain {

    public ArrayList<WHandleFilter> filters = new ArrayList<>();

    private WHandlerType code;

    public AbstractFilterChain() {
    }

    public AbstractFilterChain(WHandlerType code) {
        this.code = code;
    }

}
