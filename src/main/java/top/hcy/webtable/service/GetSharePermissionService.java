package top.hcy.webtable.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.field.WAbstractField;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.db.kv.WKVType;

import java.lang.reflect.Method;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: GetSharePermissionService
 * @Author: hcy
 * @Description:
 * @Date: 20-2-4 22:43
 * @Version: 1.0
 **/
public class GetSharePermissionService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        JSONArray tables = (JSONArray) kvDBUtils.getValue(username + ".tables", WKVType.T_LIST);

        int size = tables.size();
        JSONObject res = new JSONObject();

        for (int i = 0; i < size; i++) {
            JSONObject table = (JSONObject)kvDBUtils.getValue(username + "." + WConstants.PREFIX_TABLE + tables.get(i), WKVType.T_MAP);
            JSONObject item =new JSONObject();
            item.put("permission",table.get("permission"));
            item.put("alias",table.get("alias"));
            JSONArray fields  = (JSONArray)table.get("fields");
            JSONObject fieldsAlias = new JSONObject();
            int fieldsSize = fields.size();
            for (int j = 0; j < fieldsSize; j++) {
                JSONObject field = (JSONObject)kvDBUtils.getValue(username + "." + WConstants.PREFIX_FIELD + tables.get(i)+"."+fields.get(j), WKVType.T_MAP);
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