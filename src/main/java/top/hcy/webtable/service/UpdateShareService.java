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
 * @ClassName: UpdateShareService
 * @Author: hcy
 * @Description:
 * @Date: 20-2-5 1:28
 * @Version: 1.0
 **/
public class UpdateShareService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        JSONObject params = ctx.getParams();

        JSONArray shareslist = (JSONArray) kvDBUtils.getValue("shareslist", WKVType.T_LIST);
        if (shareslist == null){
            shareslist = new JSONArray();
        }

        String marks = params.getString("marks");
        String username = params.getString("username");
        String status = params.getString("status");
        JSONArray permissions = params.getJSONArray("permission");
        boolean flag = false;

        int size = shareslist.size();

        for (int i = 0; i < size; i++) {
            JSONObject o = (JSONObject)shareslist.get(i);
            if (o.getString("username").equals(username)){
                o.put("marks",marks);
                o.put("status",status);
                o.put("permission",permissions);
                shareslist.remove(i);
                shareslist.add(o);
                flag = true;
                break;
            }
        }

        if (flag == false){
            ctx.setWRespCode(WRespCode.UPDATE_NODATA);
            return;
        }

        boolean b = kvDBUtils.setValue("shareslist", shareslist, WKVType.T_LIST);

        if(b == false){
            ctx.setWRespCode(WRespCode.UPDATE_FAIL);
            return;
        }

        ctx.setWRespCode(WRespCode.UPDATE_SUCCESS);
    }
}