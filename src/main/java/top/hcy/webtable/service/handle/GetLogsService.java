package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.service.WService;


import static top.hcy.webtable.common.constant.WGlobal.wLogger;

@WHandleService(WHandlerType.GLOGS)
public class GetLogsService implements WService {

    String permissions[] = {};


    @Override
    public void verifyParams(WebTableContext ctx) {
        JSONArray userPermissions = ctx.getPermissions();
        for (int i = 0; i < permissions.length; i++) {
            if (userPermissions!=null && userPermissions.contains(permissions[i])){

            }else{
                ctx.setError(true);
                ctx.setWRespCode(WRespCode.PERMISSION_DENIED);
            }
        }
    }

    @Override
    public void doService(WebTableContext ctx) {

        JSONObject params = ctx.getParams();
        String user = params.getString("user");
        String role = params.getString("role");
        Integer level = params.getInteger("level");
        Long start = params.getLong("start");
        Long end = params.getLong("end");

        JSONArray logs = wLogger.getLogs(user, role, start, end, level);

        ctx.setRespsonseEntity(logs);

    }
}
