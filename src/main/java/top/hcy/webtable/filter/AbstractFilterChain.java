package top.hcy.webtable.filter;

import lombok.Data;
import top.hcy.webtable.common.enums.WHandlerTypeCode;

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
public class AbstractFilterChain implements WFilterChain {
    private WHandlerTypeCode code;

    public AbstractFilterChain() {
    }

    public AbstractFilterChain(WHandlerTypeCode code) {
        this.code = code;
    }

}
