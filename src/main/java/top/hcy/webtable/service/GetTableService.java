package top.hcy.webtable.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.db.mysql.WSelectSql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: GetTableService
 * @Author: hcy
 * @Description:
 * @Date: 20-1-28 22:16
 * @Version: 1.0
 **/
@Slf4j
public class GetTableService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        ArrayList<String> tables = WGlobal.tables;
        int size = tables.size();
        for (int i = 0; i < size; i++) {
            String table = tables.get(i);
            JSONObject tableOb = (JSONObject) kvDBUtils.getValue(username+"."+ WConstants.PREFIX_TABLE + table, WKVType.T_MAP);
            if (tableOb == null){
                continue;
            }
            JSONArray fields = (JSONArray) tableOb.get("fields");
            JSONArray permission = (JSONArray) tableOb.get("permission");
            String tableName = (String)tableOb.get("table");
            String className = (String)tableOb.get("intactClass");
            String insertTrigger = (String)tableOb.get("selectTrigger");
            String alias = (String)tableOb.get("alias");

            WSelectSql sql = new WSelectSql();
            String[] s = new String[1];
            sql.table(tableName);
            HashMap<String,String[]> fieldMap = new HashMap<>();
            for (int j = 0; j < fields.size(); j++) {
                JSONObject value = (JSONObject) kvDBUtils.getValue(username + "." + WConstants.PREFIX_FIELD+table+"."+ fields.getString(j), WKVType.T_MAP);
                if (value == null){
                    continue;
                }
                sql.fields(value.getString("column"));
                String[] fieldData = new String[2];
                fieldData[0] = value.getString("column");
                fieldData[1] = value.getString("toShowMethod");
                fieldMap.put(fields.getString(j),fieldData);
            }
            sql.limit(3, 2);
            System.out.println(sql.getSql());
            ArrayList<HashMap<String, Object>> data = sql.executeQuery();
            int length = fields.size();
            Class<?> c = null;
            try {
                c = Class.forName(className);
                int i1 = data.size();
                for (int k = 0; k < i1 ; k++) {
                    HashMap<String, Object> map = data.get(k);
                    Object o = c.newInstance();
                    for (int j = 0; j < length; j++) {
                        String filedName = fields.getString(j);
                        Field field = c.getDeclaredField(filedName);
                        if (field!=null){
                            field.setAccessible(true);
                            field.set(o,map.get(fieldMap.get(filedName)[0]));
                        }
                    }
                    //执行展示方法
                    for (int j = 0; j < length; j++) {
                        String filedName = fields.getString(j);
                        if (fieldMap.get(filedName)[1]!=null && !"".equals(fieldMap.get(filedName)[1])){
                            //无参数或者 带一个object参数
                            Method method = c.getDeclaredMethod(fieldMap.get(filedName)[1]);
                            Object out = null;
                            if(method == null){
                                method = c.getDeclaredMethod(fieldMap.get(filedName)[1],Object.class);
                                out =  method.invoke(map.get(fieldMap.get(filedName)[0]));
                            }else{
                                out = method.invoke(o);
                            }
                            Field field = c.getDeclaredField(filedName);
                            if (out!=null){
                                map.put(fieldMap.get(filedName)[0],out);
                            }else{
                                field.setAccessible(true);
                                map.put(fieldMap.get(filedName)[0],field.get(o));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("service "+e +" className: "+className);
                continue;
            }
            ctx.setRespsonseEntity(data);
        }
    }
}