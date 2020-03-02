package top.hcy.webtable.service.handle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.db.mysql.WSelectSql;
import top.hcy.webtable.db.mysql.WTableData;
import top.hcy.webtable.db.mysql.WUpdateSql;
import top.hcy.webtable.service.WService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

@WHandleService(WHandlerType.UTABLE)
public class UpdateTableDataService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        if (table == null){
            ctx.setError(true);
            ctx.setWRespCode(WRespCode.TABLE_NULL);
        }

        JSONObject fields = params.getJSONObject("fields");
        if (fields == null || fields.size() == 0){
            ctx.setWRespCode(WRespCode.FIELD_UNFAMILIAR);
            ctx.setError(true);
        }

        JSONObject pks = params.getJSONObject("pk");
        if (fields == null || pks.size() == 0){
            ctx.setWRespCode(WRespCode.PK_UNFAMILIAR);
            ctx.setError(true);
        }

    }

    @Override
    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        JSONObject updateFields = params.getJSONObject("fields");

        JSONObject pks = params.getJSONObject("pk");
        JSONObject tableData = (JSONObject) kvDBUtils.getValue(username+"."+WConstants.PREFIX_TABLE+table,WKVType.T_MAP);
        if (tableData == null){
            ctx.setWRespCode(WRespCode.TABLE_NULL);
            ctx.setError(true);
            return;
        }
        String tableName = tableData.getString("table");
        WTableData wTableData = new WTableData();
        ArrayList<String> primayKey = wTableData.table(tableName).getPrimayKey();

        check(ctx, username, table, updateFields,pks,tableData,primayKey);
        if (ctx.isError()){
            return;
        }

        //先根据主键查到 然后 在触发触发器 然后插入
        WSelectSql selectSql = new WSelectSql();
        selectSql.table(tableName);

        int pkSize = primayKey.size();
        String[] pkValue = new String[pkSize];
        selectSql.fields("*");
        selectSql.where();
        for (int i = 0; i < pkSize; i++) {
            selectSql.and(primayKey.get(i));
            pkValue[i] = pks.getString(primayKey.get(i));
        }
        ArrayList<HashMap<String, Object>> selectData = selectSql.executeQuery(pkValue);

        if (selectData.size() == 0){
            ctx.setWRespCode(WRespCode.UPDATE_NODATA);
            ctx.setError(true);
            return;
        }

        HashMap<String, Object> data = selectData.get(0);

        String className = tableData.getString("intactClass");

        try {
            Class<?> c = Class.forName(className);
            Object o = c.newInstance();
            Field[] cFields = c.getDeclaredFields();
            HashMap<String,String> fieldMap = new HashMap<>();
            for (int i = 0; i < cFields.length; i++) {
                JSONObject fieldData = getFieldData(table,username,cFields[i].getName());
                String column = cFields[i].getName();
                if (fieldData!=null){
                     column = fieldData.getString("column");
                }
                fieldMap.put(column,cFields[i].getName());
                cFields[i].setAccessible(true);
                try {
                    cFields[i].set(o,data.get(column));
                }catch (Exception e){

                }
            }
            //更新对象
           for(String key:updateFields.keySet()){
               Field updateField = c.getDeclaredField(fieldMap.get(key));
               if(updateField != null){

                   updateField.setAccessible(true);

                   String type = updateField.getType().getName();

                   if ("int".equals(type)){
                       updateField.set(o,Integer.valueOf(updateFields.get(key).toString()));
                   }else if ("long".equals(type)){
                       updateField.set(o,Long.valueOf(updateFields.get(key).toString()));
                   }else if ("double".equals(type)){
                       updateField.set(o,Double.valueOf(updateFields.get(key).toString()));
                   }else if ("float".equals(type)){
                       updateField.set(o,Float.valueOf(updateFields.get(key).toString()));
                   }else{
                       updateField.set(o,updateFields.get(key).toString());
                   }

               }
               JSONObject fieldData = getFieldData(table,username,updateField.getName());
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
                       out =  method.invoke(o,updateFields.get(key));
                   }catch (Exception e){

                   }
               }else{
                   out = method.invoke(o);
               }
               if (out!=null){
                   updateFields.put(key,out);
               }else{
                   updateField.setAccessible(true);
                   updateFields.put(key,updateField.get(o));
               }
           }

          //进行更新操作

            WUpdateSql updateSql  = new WUpdateSql();
            updateSql.table(tableName).where();

            for (int i = 0; i < pkSize; i++) {
                updateSql.and(primayKey.get(i));
            }

            for (String key : updateFields.keySet()){
                updateSql.update(key,updateFields.getString(key));
            }
            int i = updateSql.executeUpdate(pkValue);

            if (i == 1){
                ctx.setWRespCode(WRespCode.UPDATE_SUCCESS);
                //更新触发器
                String updateTrigger = tableData.getString("updateTrigger");
                System.out.println(updateTrigger);
                if (updateTrigger!=null&& updateTrigger.length()>0){
                    Method trigger = null;
                    try{
                     trigger =  c.getDeclaredMethod(updateTrigger,WebTableContext.class);
                    }catch (Exception e){

                    }
                  if (trigger!=null){
                      trigger.invoke(o,ctx);
                  }
                }

            }else{
                ctx.setWRespCode(WRespCode.UPDATE_FAIL);
                ctx.setError(true);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private JSONObject getFieldData(String table, String username, String field) {
        return (JSONObject) kvDBUtils.getValue( WConstants.PREFIX_FIELD+table+"."+ field, WKVType.T_MAP);
    }
    private void check(WebTableContext ctx, String username, String table, JSONObject updateFields, JSONObject pks, JSONObject tableData,ArrayList<String> primayKey) {

        //验证表权限
        JSONArray permission = tableData.getJSONArray("permission");

        if (!permission.contains("update")){
            ctx.setWRespCode(WRespCode.PERMISSION_DENIED);
            ctx.setError(true);
            return;
        }

        //验证表主键完备
//        System.out.println(primayKey);
        int size = primayKey.size();
        for (int i = 0; i < size; i++) {
            if(!pks.containsKey(primayKey.get(i))){
                ctx.setWRespCode(WRespCode.PK_UNFAMILIAR);
                ctx.setError(true);
                return;
            }
        }

        //验证字段正确 和 权限
        String intactClass =  tableData.getString("intactClass");

        try {
            Class<?> c = Class.forName(intactClass);
            Set<String> keySet = updateFields.keySet();
            Field[] fields = c.getFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                JSONObject fieldData = (JSONObject) kvDBUtils.getValue( WConstants.PREFIX_FIELD + table + "." + field, WKVType.T_MAP);
                if (fieldData.getString("fieldPermission").contains("update")){
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