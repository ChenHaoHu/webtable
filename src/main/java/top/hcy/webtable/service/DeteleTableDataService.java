package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.db.mysql.WDeleteSql;
import top.hcy.webtable.db.mysql.WInsertSql;
import top.hcy.webtable.db.mysql.WTableData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: UpdateTableDataService
 * @Author: hcy
 * @Description: 更新数据
 * @Date: 20-1-30 23:42
 * @Version: 1.0
 **/
public class DeteleTableDataService implements WService{
    @Override
    public void verifyParams(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        if (table == null){
            ctx.setError(true);
            ctx.setWRespCode(WRespCode.TABLE_NULL);
        }

        JSONObject pkFields = params.getJSONObject("fields");
        if (pkFields == null || pkFields.size() == 0){
            ctx.setError(true);
            ctx.setWRespCode(WRespCode.FIELD_UNFAMILIAR);
        }
    }

    @Override
    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        JSONObject pkFields = params.getJSONObject("fields");

        JSONObject tableData = (JSONObject) kvDBUtils.getValue(username+"."+WConstants.PREFIX_TABLE+table,WKVType.T_MAP);
        if (tableData == null){
            ctx.setWRespCode(WRespCode.TABLE_NULL);
            return;
        }
        String tableName = tableData.getString("table");
        WTableData wTableData = new WTableData();
        ArrayList<String> primayKey = wTableData.table(tableName).getPrimayKey();

        check(ctx, pkFields,tableData,primayKey);
        if (ctx.isError()){
            return;
        }

        //删除操作
        WDeleteSql wDeleteSql = new WDeleteSql();
        wDeleteSql.table(tableName);
        wDeleteSql.where();
        int size = pkFields.size();
        String[] values = new String[size];
        int i = 0;
        wDeleteSql.where();
        for (String key : pkFields.keySet()){
            wDeleteSql.and(key);
            values[i++] = pkFields.getString(key);
        }

        int i1 = wDeleteSql.executeDelete(values);

        if (i1 >= 1){
            ctx.setWRespCode(WRespCode.DELETE_SUCCESS);
            String cClassName = tableData.getString("intactClass");
            String deleteTrigger = tableData.getString("deleteTrigger");
            if (deleteTrigger!=null && deleteTrigger.length()>0){
                try {
                    Class<?> c = Class.forName(cClassName);
                    Method method = null;
                    try{
                        method =  c.getDeclaredMethod(deleteTrigger, WebTableContext.class);
                    }catch (Exception e){

                    }
                    method.invoke(c.newInstance(),ctx);

                    if (method!=null){

                    }

                } catch (Exception e) {

                }
            }


        }else{
            ctx.setWRespCode(WRespCode.DELETE_FAIL);
        }

    }

    private JSONObject getFieldData(String table, String username, String field) {
        return (JSONObject) kvDBUtils.getValue( WConstants.PREFIX_FIELD+table+"."+ field, WKVType.T_MAP);
    }

    private void check(WebTableContext ctx, JSONObject pks, JSONObject tableData, ArrayList<String> primayKey) {

        //验证表权限
        JSONArray permission = tableData.getJSONArray("permission");

        if (!permission.contains("delete")){
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

    }

}