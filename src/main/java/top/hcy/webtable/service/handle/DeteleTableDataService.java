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
import top.hcy.webtable.wsql.structured.WDeleteSql;
import top.hcy.webtable.wsql.structured.factory.WSQLFactory;
import top.hcy.webtable.wsql.structured.impl.mysql.WMySQLTableData;
import top.hcy.webtable.service.WService;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


@WHandleService(WHandlerType.DTABLE)
@WEnableLog
public class DeteleTableDataService extends WService {

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


    public void doService(WebTableContext ctx) {
        String username = ctx.getUsername();
        JSONObject params = ctx.getParams();
        String table = params.getString("table");
        JSONObject pkFields = params.getJSONObject("fields");

        JSONObject tableData = (JSONObject) kvDBUtils.getValue(username+"."+WConstants.PREFIX_TABLE+table,WKVType.T_MAP);
        if (tableData == null){
            ctx.setWRespCode(WRespCode.TABLE_NULL);
            ctx.setError(true);
            return;
        }
        String tableName = tableData.getString("table");
        WMySQLTableData wMysqlTableData = new WMySQLTableData();
        ArrayList<String> primayKey = wMysqlTableData.table(tableName).getPrimayKey();

        check(ctx, pkFields,tableData,primayKey);
        if (ctx.isError()){
            return;
        }

        //删除操作
       // WMysqlDeleteSql wMysqlDeleteSql = new WMysqlDeleteSql();
        WDeleteSql wDeleteSql = WSQLFactory.getWDeleteSql(ctx.getWsqldbType());
        wDeleteSql.table(tableName);
        wDeleteSql.where();
        int size = pkFields.size();
        int i = 0;
        wDeleteSql.where();
        for (String key : pkFields.keySet()){
            wDeleteSql.and(key,pkFields.getString(key));
        }

        int i1 = wDeleteSql.executeDelete();

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
            ctx.setError(true);
        }

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