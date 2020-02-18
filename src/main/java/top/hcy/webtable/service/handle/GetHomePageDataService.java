package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.charts.WChart;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.service.WService;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


@WHandleService(WHandlerType.GHOMEPAGE)
public class GetHomePageDataService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        String role = ctx.getRole();
        JSONArray array = new JSONArray();
        JSONArray tables = (JSONArray)kvDBUtils.getValue(username+".tables", WKVType.T_LIST);
        for (int i = 0; i < tables.size(); i++) {
            String table = tables.getString(i);
            JSONObject tableData = getTable(table, username);
            JSONObject wcharts = tableData.getJSONObject("wchart");
            if (wcharts == null){
                continue;
            }
            Set<String> charts = wcharts.keySet();
            for (String key : charts){
                JSONObject wchart = wcharts.getJSONObject(key);
                if (wchart.getBoolean("showDashboard") == true){
                    JSONObject chartData = getChartData(username, table, wchart.getString("method"));
                    if (chartData!=null){
                        array.add(chartData);
                    }
                }
            }

        }
        ctx.setRespsonseEntity(array);
    }

    private JSONObject getChartData( String username, String table, String chart) {
        JSONObject data  = null;
        JSONObject tableData = getTable(table, username);
        if (tableData == null){

            return null;
        }
        JSONArray permission = tableData.getJSONArray("permission");
        if (!permission.contains("chart")){
            return null;
        }

        String intactClass = tableData.getString("intactClass");
        try {
            Class<?> c = Class.forName(intactClass);
            Method method = null;
            WChart annotation = null;
            try {
                method = c.getMethod(chart);
                annotation = method.getAnnotation(WChart.class);
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
                data = new JSONObject();
                data.put("chart",out);
                data.put("title",null);
                if (annotation!=null){
                    data.put("title",annotation.value());
                }
                return  data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    private JSONObject getFieldData(String table, String username, String field) {
        return (JSONObject) kvDBUtils.getValue( WConstants.PREFIX_FIELD+table+"."+ field, WKVType.T_MAP);
    }

    private JSONObject getTable(String table, String username) {
        return (JSONObject) kvDBUtils.getValue(username+"."+ WConstants.PREFIX_TABLE + table, WKVType.T_MAP);
    }
}