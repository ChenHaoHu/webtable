package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.service.WService;

import java.util.ArrayList;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: DeleteShareService
 * @Author: hcy
 * @Description:
 * @Date: 20-2-5 1:43
 * @Version: 1.0
 **/
@WHandleService(WHandlerType.DSHAREDATA)
public class DeleteShareService implements WService {
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
            return;
        }

        boolean b = kvDBUtils.setValue("shareslist", shareslist, WKVType.T_LIST);

        if(b == false){
            ctx.setWRespCode(WRespCode.DELETE_FAIL);
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