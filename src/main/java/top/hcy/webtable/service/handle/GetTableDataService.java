package top.hcy.webtable.service.handle;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.annotation.field.WAbstractField;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.common.enums.WebFieldType;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.db.mysql.WSelectSql;
import top.hcy.webtable.db.mysql.WTableData;
import top.hcy.webtable.service.WService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


@Slf4j
@WHandleService(WHandlerType.GTABLE)
public class GetTableDataService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        Integer pagesize = params.getInteger("pagesize");
        pagesize = pagesize == null?WConstants.PAGE_SIZE:pagesize;
        params.put("pagesize",pagesize);
        Integer pagenum = params.getInteger("pagenum");
        pagenum = pagenum == null?WConstants.PAGE_NUM:pagenum;
        params.put("pagenum",pagenum);
        String table = params.getString("table");
        if (table == null){
            ctx.setError(true);
            ctx.setWRespCode(WRespCode.TABLE_NULL);
        }
    }

    @Override
    public void doService(WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        HashMap<String, Object> table = getTableData(params.getString("table"),params.getInteger("pagenum"),params.getInteger("pagesize"), ctx);
        ctx.setRespsonseEntity(table);
    }

    private HashMap<String,Object> getTableData( String table,int pagenum, int pagesize,WebTableContext ctx) {
        JSONObject params = ctx.getParams();
        String username = ctx.getUsername();
        JSONObject tableOb = getTable(table, username);
        if (tableOb == null){
            return null;
        }
        HashMap<String,Object> res = new HashMap<>();
        ArrayList<HashMap<String, Object>>  data = null;

        JSONArray fields = (JSONArray) tableOb.get("fields");
        JSONArray permission = (JSONArray) tableOb.get("permission");
        String tableName = (String)tableOb.get("table");
        String className = (String)tableOb.get("intactClass");
        String selectTrigger = (String)tableOb.get("selectTrigger");
        JSONArray abstractfields = (JSONArray)tableOb.get("abstractfields");
        String alias = (String)tableOb.get("alias");
        res.put("alias",alias);
        res.put("permission",permission);

        ArrayList<HashMap<String, Object>> totalSql = new WSelectSql(tableName).count().executeQuery();

        Object total = totalSql.size() > 0?totalSql.get(0).get("count"):0;
        res.put("total",Integer.valueOf(total.toString()));

        WSelectSql sql = new WSelectSql();
        sql.table(tableName);
        HashMap<String,String[]> fieldMap = new HashMap<>();
        //HashMap< String,HashMap<String,Object>> fieldsMap = new HashMap<>();
        //不让乱序
        LinkedHashMap fieldsMap = new LinkedHashMap();
        res.put("fields",fieldsMap);

        for (int j = 0; j < fields.size(); j++) {
            JSONObject value = getFieldData(table, username, fields.getString(j));
            if (value == null){
                continue;
            }
            String columnName = value.getString("column");
            sql.fields(columnName);
            String[] fieldData = new String[2];
            fieldData[0] = columnName;
            fieldData[1] = value.getString("toShowMethod");

            fieldMap.put(fields.getString(j),fieldData);


            JSONArray fieldPermissions = value.getJSONArray("fieldPermission");

            boolean read = fieldPermissions.contains("read");

            if (read){
                HashMap<String,Object> map = new HashMap<>();
                map.put("alias",value.getString("alias"));
                map.put("field",value.getString("field"));
                map.put("webFieldType",value.getString("webFieldType"));
                map.put("fieldPermission",value.get("fieldPermission"));
                map.put("selects",value.get("selects"));
                fieldsMap.put(columnName,map);
            }
        }

        if (pagenum < 1){
            pagenum  = 1;
        }

        sql.limit(pagesize, (pagenum-1)*pagesize);

        //添加主键 保证唯一性
        WTableData wTableData = new WTableData();
        ArrayList<String> primayKey = wTableData.table(tableName).getPrimayKey();

        int pkSize = primayKey.size();
        for (int i = 0; i < pkSize; i++) {
            sql.fieldsPk(primayKey.get(i));
        }

        res.put("pk",primayKey);

        String[] queryValue = new String[0];

        sql.where();

        //处理 查找条件
        JSONArray findData = params.getJSONArray("find");

        if (findData !=null && findData.size()>0){
            boolean find = permission.contains("find");
            if (find){

                int size = findData.size();
                queryValue = new String[size];

                for (int i = 0; i < size; i++) {
                    JSONObject item = findData.getJSONObject(i);
                    String compare = item.getString("compare");
                    String key = item.getString("key");
                    String value = item.getString("value");

                    switch (compare){
                        case  "equals":{
                            queryValue[i] =value;
                            sql.and(key);
                            break;
                        }
                        case  "like":{
                            sql.like(key);
                            queryValue[i] = "%"+value+"%";
                            break;
                        }
                        case  "greater":{
                            queryValue[i] =value;
                            sql.greater(key);
                            break;
                        }
                        case  "less":{
                            queryValue[i] =value;
                            sql.less(key);
                            break;
                        }
                        case  "greaterAndequals":{
                            queryValue[i] =value;
                            sql.greaterAndequals(key);
                            break;
                        }
                        case  "lessAndequals":{
                            queryValue[i] =value;
                            sql.lessAndequals(key);
                            break;
                        }
                    }
                }
            }else{
                ctx.setWRespCode(WRespCode.PERMISSION_DENIED);
                return null;
            }
        }

        //处理 排序条件
        JSONObject sortData = params.getJSONObject("sort");

        if(sortData!=null) {
            boolean sort = permission.contains("sort");
            if (sort){

                String field = sortData.getString("field");
                Boolean desc = sortData.getBoolean("desc");

                if (desc !=null && field!=null){
                    sql.orderBy(field,desc);
                }else if(field!=null){
                    sql.orderBy(field);
                }

            }else{
                ctx.setWRespCode(WRespCode.PERMISSION_DENIED);
                return null;
            }
        }



        data = sql.executeQuery(queryValue);

        int length = fields.size();
        Class<?> c = null;
        try {
            c = Class.forName(className);
            int i1 = data.size();
            //不处理 附加主键
            i1 = i1-pkSize;
            for (int k = 0; k < i1 ; k++) {
                HashMap<String, Object> map = data.get(k);
                Object o = c.newInstance();
                for (int j = 0; j < length; j++) {
                    String filedName = fields.getString(j);
                    Field field = c.getDeclaredField(filedName);
                    if (field!=null){
                        field.setAccessible(true);
                        try{
                            field.set(o,map.get(fieldMap.get(filedName)[0]));
                        }catch (Exception e){

                        }
                    }
                }
                //执行展示方法
                for (int j = 0; j < length; j++) {
                    String filedName = fields.getString(j);
                    if (fieldMap.get(filedName)[1]!=null && !"".equals(fieldMap.get(filedName)[1])){
                        //无参数或者 带一个object参数
                        Method method = null;
                        try{
                            method = c.getDeclaredMethod(fieldMap.get(filedName)[1]);
                        }catch (Exception e){

                        }

                        Object out = null;
                        if(method == null){

                            try{
                                method = c.getDeclaredMethod(fieldMap.get(filedName)[1],Object.class);
                                out =  method.invoke(map.get(fieldMap.get(filedName)[0]));
                            }catch (Exception e){

                            }

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

                //获取虚拟字段
                if(abstractfields != null){
                    int abstractfieldsSize = abstractfields.size();
                    if ( abstractfieldsSize>0){

                        for (int i = 0; i < abstractfieldsSize; i++) {

                            Method method =  null;
                            Object out = null;

                            try {
                                method = c.getDeclaredMethod(abstractfields.getString(i));
                            }catch (Exception e){

                            }
                            if (method!=null){
                                WAbstractField wAbstractFieldAnnotation = method.getAnnotation(WAbstractField.class);
                                String aliasName = wAbstractFieldAnnotation.aliasName();
                                WebFieldType webFieldType = wAbstractFieldAnnotation.fieldType();
                                out = method.invoke(o);
                                map.put(abstractfields.getString(i),out);

                                HashMap<String,Object> s = new HashMap<>();
                                s.put("alias",aliasName);
                                s.put("field",abstractfields.getString(i));
                                s.put("webFieldType",webFieldType.getStr());
                                s.put("fieldPermission","");
                                s.put("selects",null);
                                fieldsMap.put(abstractfields.getString(i),s);
                            }
                        }
                    }
                }

                if(k == i1-1){
                    //执行触发器
                    if (selectTrigger!=null && selectTrigger.length()>0){
                        Method trigger =  null;
                        try {
                            trigger = c.getDeclaredMethod(selectTrigger, WebTableContext.class);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if (trigger!=null){
                            trigger.invoke(o,ctx);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("service "+e +" className: "+className);
            return null;
        }
        res.put("data",data);
        res.put("pagenum",pagenum);
        res.put("pagesize",data.size());

        if (permission.contains("chart")){
            //添加可视化数据
            JSONObject wchart = tableOb.getJSONObject("wchart");

           if (wchart!=null){
               for (String key : wchart.keySet()){
                   String method = wchart.getJSONObject(key).getString("method");
                   wchart.put(key,method);
               }
           }

            res.put("wchart",wchart);
        }


        return res;
    }

    private JSONObject getFieldData(String table, String username, String field) {
        return (JSONObject) kvDBUtils.getValue( WConstants.PREFIX_FIELD+table+"."+ field, WKVType.T_MAP);
    }

    private JSONObject getTable(String table, String username) {
        return (JSONObject) kvDBUtils.getValue(username+"."+ WConstants.PREFIX_TABLE + table, WKVType.T_MAP);
    }
}