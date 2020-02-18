package top.hcy.webtable.service;

import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.filter.AbstractFilterChain;
import top.hcy.webtable.filter.WHandleFilter;


public interface WService {

    void verifyParams(WebTableContext ctx);
    void doService(WebTableContext ctx);
}
