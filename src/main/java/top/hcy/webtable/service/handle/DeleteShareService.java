package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.annotation.webtable.WEnableLog;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.wsql.kv.WKVType;
import top.hcy.webtable.service.WService;

import java.util.ArrayList;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


@WHandleService(WHandlerType.DSHAREDATA)
@WEnableLog
public class DeleteShareService extends WService {

    public void verifyParams(WebTableContext ctx) {

    }



    public void doService(WebTableContext ctx) {
        JSONObject params = ctx.getParams();

        JSONArray shareslist = (JSONArray) kvDBUtils.getValue("shareslist", WKVType.T_LIST);
        if (shareslist == null){
            shareslist = new JSONArray();
        }
        String username = params.getString("username");
        boolean flag = false;

        int size = shareslist.size();

        for (int i = 0; i < size; i++) {
            JSONObject o = (JSONObject)shareslist.get(i);
            if (o.getString("username").equals(username)){
                shareslist.remove(i);
                flag = true;
                break;
            }
        }

        if (flag == false){
            ctx.setWRespCode(WRespCode.DELETE_NODATA);
            ctx.setError(true);
            return;
        }

        boolean b = kvDBUtils.setValue("shareslist", shareslist, WKVType.T_LIST);

        if(b == false){
            ctx.setWRespCode(WRespCode.DELETE_FAIL);
            ctx.setError(true);
            return;
        }


        ArrayList<String> allKeys = kvDBUtils.getAllKeys();

        for (int i = 0; i < allKeys.size(); i++) {
            String s = allKeys.get(i);
            if (s.startsWith(username)){
                kvDBUtils.deleKey(s);
            }
        }

        ctx.setWRespCode(WRespCode.DELETE_SUCCESS);
    }
}