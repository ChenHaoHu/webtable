package top.hcy.webtable.logs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.tools.CommonUtils;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

public class WLogger implements WLogs {

    @Override
    public  void info( WebTableContext ctx, String str) {
        saveLogs(ctx, 0);
    }

    @Override
    public  void warn(WebTableContext ctx, String str)  {
        int level = 1;
        saveLogs(ctx, level);
    }


    @Override
    public  void error(WebTableContext ctx, String str)  {
        int level = 2;
        saveLogs(ctx, level);
    }


    @Override
    public JSONArray getLogs(String user, String role, Long start, Long end, Integer level) {
        long  k = 24 * 60 * 60 ;
        if (level == null){
            level = -1;
        }

        if (start == null){
            start = CommonUtils.getTodayStartTime();
        }
        if (end == null){
            end = CommonUtils.getTodayStartTime() + k;
        }

        JSONArray combineLogs = new JSONArray();

        for (long i = start; i <= end; i = i+k) {
            JSONArray logs = (JSONArray) kvDBUtils.getValue(WConstants.PREFIX_LOG+i, WKVType.T_LIST);
            if (logs!=null){
                combineLogs.addAll(logs);
            }
        }
        int size = combineLogs.size();
        JSONArray res = new JSONArray();
        for (int i = 0; i < size; i++) {
            WLogEventEntity entity =JSON.toJavaObject(combineLogs.getJSONObject(i),WLogEventEntity.class);
            if ( level == -1 || entity.getLevel() == level){
                if (user == null || user.isEmpty() || (user!=null && user.equals(entity.getUsername())) ){
                    if (role == null || role.isEmpty()  || (role!=null && role.equals(entity.getRole())) ){
                        res.add(0,entity);
                    }
                }
            }
        }

        return res;
    }

    private synchronized void saveLogs(WebTableContext ctx, int level) {

        String[] logWhiteList = WGlobal.logWhiteList;
        int logWhiteListLength = logWhiteList.length;
        for (int i = 0; i < logWhiteListLength; i++) {
              if (logWhiteList[i].equals(ctx.getRealUri())){
                  return;
              }
        }

        Long s = CommonUtils.getTodayStartTime();
        String key = WConstants.PREFIX_LOG+s;

        JSONArray logs = (JSONArray) kvDBUtils.getValue(key, WKVType.T_LIST);
        if (logs == null) {
            logs = new JSONArray();
        }
        String realUri = ctx.getRealUri();
        String requstDesc = "";
        WHandlerType[] wHandlerTypes = WHandlerType.values();
        int length = wHandlerTypes.length;
        for (int i = 0; i < length; i++) {
            WHandlerType wHandlerType = wHandlerTypes[i];
            if (realUri.equals(wHandlerType.getUri())){
                requstDesc = wHandlerType.getDesc();
                break;
            }
        }

        //打日志时，记录返回时间  有待改进
        ctx.setResponseTime(System.currentTimeMillis());

        WLogEventEntity wLogEventEntity = new WLogEventEntity(level, ctx.getUsername(), ctx.getRole(),
                ctx.getRealUri(),requstDesc, ctx.getParams().toJSONString(), ctx.getWRespCode().getMsg(),ctx.getRequestTime(),
        ctx.getResponseTime(),ctx.getExecutedSQLs());
        logs.add(wLogEventEntity);
        kvDBUtils.setValue(key, logs, WKVType.T_LIST);
    }

}