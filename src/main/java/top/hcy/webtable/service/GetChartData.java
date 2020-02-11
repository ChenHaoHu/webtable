package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: GetChartData
 * @Author: hcy
 * @Description:
 * @Date: 20-2-11 19:30
 * @Version: 1.0
 **/
public class GetChartData implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

        JSONObject params = ctx.getParams();
        if (!params.containsKey("table")||!params.containsKey("chart")){
            ctx.setWRespCode(WRespCode.REQUEST_PARAM_WARN);
            ctx.setError(true);
        }
    }

    @Override
    public void doService(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        String chart = params.getString("chart");
        String username = ctx.getUsername();
        JSONObject tableData = getTable(table, username);
        if (tableData == null){
            ctx.setWRespCode(WRespCode.TABLE_NULL);
            return;
        }
        JSONArray permission = tableData.getJSONArray("permission");
        if (!permission.contains("chart")){
            ctx.setWRespCode(WRespCode.PERMISSION_DENIED);
            return;
        }

        String intactClass = tableData.getString("intactClass");
        try {
            Class<?> c = Class.forName(intactClass);
            Method method = null;
            try {
                method = c.getMethod(chart);
            }catch (Exception e){

            }
            Object out = null;
            if (method!=null){
                int modifiers = method.getModifiers();
                boolean aStatic = Modifier.isStatic(modifiers);
                if (aStatic){
                    out = method.invoke(null);
                }else {
                    out = method.invoke(c.newInstance());
                }
                ctx.setRespsonseEntity(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getTable(String table, String username) {
        return (JSONObject) kvDBUtils.getValue(username+"."+ WConstants.PREFIX_TABLE + table, WKVType.T_MAP);
    }
}