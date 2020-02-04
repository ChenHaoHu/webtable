package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.tools.CommonUtils;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: AddShareService
 * @Author: hcy
 * @Description:
 * @Date: 20-2-5 0:49
 * @Version: 1.0
 **/
public class AddShareService implements  WService{
    @Override
    public void verifyParams(WebTableContext ctx) {
        //验证管理员身份

    }

    @Override
    public void doService(WebTableContext ctx) {


        JSONArray shareslist = (JSONArray) kvDBUtils.getValue("shareslist", WKVType.T_LIST);

        if (shareslist == null){
            shareslist = new JSONArray();
        }

        JSONObject data = CommonUtils.generateShareAccount();
        JSONObject params = ctx.getParams();
        String marks = params.getString("marks");
        Object permission = params.get("permission");
        data.put("marks",marks);
        data.put("status","1");
        data.put("permission",permission);
        shareslist.add(data);

        boolean b = kvDBUtils.setValue("shareslist", shareslist, WKVType.T_LIST);

        if(b == false){
            ctx.setWRespCode(WRespCode.INSERT_FAIL);
            return;
        }

        ctx.setRespsonseEntity(data);
        ctx.setWRespCode(WRespCode.INSERT_SUCCESS);
    }
}