package top.hcy.webtable.filter;

import lombok.Data;
import top.hcy.webtable.router.WHandlerType;

import java.util.ArrayList;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.filter
 * @ClassName: AbstractFilterChain
 * @Author: hcy
 * @Description: Abstract
 * @Date: 2020/1/15 13:13
 * @Version: 1.0
 */
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
