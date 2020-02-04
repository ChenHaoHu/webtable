package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONArray;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.db.kv.WKVType;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;
/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: GetShareService
 * @Author: hcy
 * @Description:
 * @Date: 20-2-5 1:08
 * @Version: 1.0
 **/
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