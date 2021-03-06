package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.annotation.field.WAbstractField;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.wsql.kv.WKVType;
import top.hcy.webtable.service.WService;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


@WHandleService(WHandlerType.GSHAREPERMISSIONLIST)
public class GetSharePermissionService extends WService {

    public void verifyParams(WebTableContext ctx) {

    }


    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        JSONArray tables = (JSONArray) kvDBUtils.getValue(username + ".tables", WKVType.T_LIST);

        int size = tables.size();
        JSONObject res = new JSONObject();

        for (int i = 0; i < size; i++) {
            JSONObject table = (JSONObject)kvDBUtils.getValue(username + "." + WConstants.PREFIX_TABLE + tables.get(i), WKVType.T_MAP);
            JSONObject item =new JSONObject();
            item.put("permission",table.get("permission"));

            JSONObject wchart = table.getJSONObject("wchart");
           if (wchart!=null){
               for (String key : wchart.keySet()){
                   String method = wchart.getJSONObject(key).getString("method");
                   wchart.put(key,method);
               }
           }
            item.put("wchart",wchart);
            item.put("alias",table.get("alias"));
            JSONArray fields  = (JSONArray)table.get("fields");
            LinkedHashMap<String,String> fieldsAlias = new LinkedHashMap();
            int fieldsSize = fields.size();
            for (int j = 0; j < fieldsSize; j++) {
                JSONObject field = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD + tables.get(i)+"."+fields.get(j), WKVType.T_MAP);
                fieldsAlias.put(fields.getString(j),field.getString("alias"));
            }
            JSONArray wAbstractFields  = (JSONArray)table.get("abstractfields");
            if (wAbstractFields!=null){
                int wAbstractFieldsSize = wAbstractFields.size();
                String intactClass = table.getString("intactClass");
                Class<?> c = null;
                try {
                    c = Class.forName(intactClass);
                    for (int j = 0; j < wAbstractFieldsSize; j++) {
                        Method method = null;
                        if (c !=null){
                            try {
                                method = c.getDeclaredMethod(wAbstractFields.getString(j));
                            }catch (Exception e){

                            }

                            if (method !=null){
                                WAbstractField annotation = method.getAnnotation(WAbstractField.class);
                                String aliasName = annotation.aliasName();
                                fieldsAlias.put("abstract."+wAbstractFields.getString(j),aliasName);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            item.put("fields",fieldsAlias);
            res.put(tables.getString(i),item);
        }
        ctx.setRespsonseEntity(res);
    }
}