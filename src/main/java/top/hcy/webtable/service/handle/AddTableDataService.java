package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.annotation.webtable.WEnableLog;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.wsql.kv.WKVType;
import top.hcy.webtable.wsql.structured.WInsertSql;
import top.hcy.webtable.wsql.structured.factory.WSQLFactory;
import top.hcy.webtable.service.WService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


@WHandleService(WHandlerType.ATABLE)
@WEnableLog
public class AddTableDataService extends WService {

    public void verifyParams(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        if (table == null){
            ctx.setError(true);
            ctx.setWRespCode(WRespCode.TABLE_NULL);
        }

        JSONObject fields = params.getJSONObject("fields");
        if (fields == null || fields.size() == 0){
            ctx.setError(true);
            ctx.setWRespCode(WRespCode.FIELD_UNFAMILIAR);
        }


    }


    public void doService(WebTableContext ctx) {

        String username = ctx.getUsername();
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        JSONObject insertFields = params.getJSONObject("fields");
        JSONObject tableData = (JSONObject) kvDBUtils.getValue(username+"."+WConstants.PREFIX_TABLE+table,WKVType.T_MAP);

        if (tableData == null){
            ctx.setWRespCode(WRespCode.TABLE_NULL);
            ctx.setError(true);
            return;
        }
        String tableName = tableData.getString("table");
        check(ctx, username, table, insertFields,tableData);
        if (ctx.isError()){
            return;
        }

        String className = tableData.getString("intactClass");
        try {
            Class<?> c = Class.forName(className);
            Object o = c.newInstance();
            Field[] cFields = c.getDeclaredFields();
            HashMap<String,String> fieldMap = new HashMap<>();
            for (int i = 0; i < cFields.length; i++) {
                JSONObject fieldData = getFieldConfig(table,username,cFields[i].getName());
                String column = cFields[i].getName();
                if (fieldData!=null){
                    column = fieldData.getString("column");
                }
                fieldMap.put(column,cFields[i].getName());
                String varColumn = insertFields.getString(column);
                if (varColumn!=null){
                    cFields[i].setAccessible(true);

                    String type = cFields[i].getType().getName();
                    if ("int".equals(type)){
                        cFields[i].set(o,Integer.valueOf(varColumn));
                    }else if ("long".equals(type)){
                        cFields[i].set(o,Long.valueOf(varColumn));
                    }else if ("double".equals(type)){
                        cFields[i].set(o,Double.valueOf(varColumn));
                    }else if ("float".equals(type)){
                        cFields[i].set(o,Float.valueOf(varColumn));
                    }else{
                        cFields[i].set(o,varColumn);
                    }

                }
            }


            for (String key : insertFields.keySet()){
                Field insertField = c.getDeclaredField(fieldMap.get(key));
                JSONObject fieldData = getFieldConfig(table,username,insertField.getName());
                String toPersistenceMethod = fieldData.getString("toPersistenceMethod");
                if (toPersistenceMethod  == null || toPersistenceMethod.length() == 0){
                    continue;
                }
                Method method = null;
                try{
                    method = c.getDeclaredMethod(toPersistenceMethod);
                }catch (Exception e){

                }
                Object out = null;
                if(method == null){
                    try{
                        method = c.getDeclaredMethod(toPersistenceMethod,Object.class);
                        out =  method.invoke(o,insertFields.get(key));
                    }catch (Exception e){

                    }
                }else{
                    out = method.invoke(o);
                }
                if (out!=null){
                    insertFields.put(key,out);
                }else{
                    insertField.setAccessible(true);
                    insertFields.put(key,insertField.get(o));
                }
            }

            //WMysqlInsertSql wMysqlInsertSql = new WMysqlInsertSql();
            WInsertSql wInsertSql = WSQLFactory.getWInsertSql(ctx.getWsqldbType());
            wInsertSql.table(tableName);
            int size = insertFields.size();
            String[] fs = new String[size];
            int i = 0;
            for (String key : insertFields.keySet()){
                wInsertSql.fields(key);
                fs[i++] = insertFields.getString(key);
            }
            wInsertSql.values(fs);
            int i1 = wInsertSql.executeInsert();


            if (i1 >= 1){
                ctx.setWRespCode(WRespCode.INSERT_SUCCESS);
                //触发trigger
                String insertTrigger = tableData.getString("insertTrigger");
                if (insertTrigger!=null&& insertTrigger.length()>0){

                    Method trigger = null;
                    try{
                        trigger =  c.getDeclaredMethod(insertTrigger, WebTableContext.class);
                    }catch (Exception e){

                    }

                    if (trigger!=null){
                        trigger.invoke(o,ctx);
                    }
                }
            }else{
                ctx.setWRespCode(WRespCode.INSERT_FAIL);
                ctx.setError(true);
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }


    private void check(WebTableContext ctx, String username, String table, JSONObject insertFields, JSONObject tableData) {

        //验证表权限
        JSONArray permission = tableData.getJSONArray("permission");

        if (!permission.contains("insert")){
            ctx.setWRespCode(WRespCode.PERMISSION_DENIED);
            ctx.setError(true);
            return;
        }


        //验证字段正确 和 权限
        String intactClass =  tableData.getString("intactClass");

        try {
            Class<?> c = Class.forName(intactClass);
            Set<String> keySet = insertFields.keySet();
            Field[] fields = c.getFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                JSONObject fieldData = (JSONObject) kvDBUtils.getValue( WConstants.PREFIX_FIELD + table + "." + field, WKVType.T_MAP);
                if (fieldData.getString("fieldPermission").contains("insert")){
                    String column = fieldData.getString("column");
                    if (!keySet.contains(column)){
                        ctx.setWRespCode(WRespCode.FIELD_UNFAMILIAR);
                        ctx.setError(true);
                        return;
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            ctx.setWRespCode(WRespCode.FIELD_UNFAMILIAR);
            ctx.setError(true);
            return;
        }



    }
}