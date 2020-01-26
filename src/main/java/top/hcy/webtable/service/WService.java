package top.hcy.webtable.service;

import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.filter.AbstractFilterChain;
import top.hcy.webtable.filter.WHandleFilter;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.filter
 * @ClassName: FiterChain
 * @Author: hcy
 * @Description: Handler failter
 * @Date: 2020/1/15 11:01
 * @Version: 1.0
 */
public interface WService {

    void verifyParams(WebTableContext ctx);
    void doService(WebTableContext ctx);
}
