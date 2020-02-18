package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.service.WService;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

@WHandleService(WHandlerType.GSHAREDATA)
public class GetShareService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        JSONArray shareslist = (JSONArray) kvDBUtils.getValue("shareslist", WKVType.T_LIST);
        ctx.setRespsonseEntity(shareslist);
    }
}