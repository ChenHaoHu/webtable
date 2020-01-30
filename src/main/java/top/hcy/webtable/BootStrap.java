package top.hcy.webtable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.util.ConfigurationBuilder;
import top.hcy.webtable.annotation.field.*;
import top.hcy.webtable.annotation.method.WDeleteTrigger;
import top.hcy.webtable.annotation.method.WInsertTrigger;
import top.hcy.webtable.annotation.method.WSelectTrigger;
import top.hcy.webtable.annotation.table.WEnadbleDelete;
import top.hcy.webtable.annotation.table.WEnadbleInsert;
import top.hcy.webtable.annotation.table.WEnadbleUpdate;
import top.hcy.webtable.annotation.table.WTable;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.db.kv.WKvDbUtils;
import top.hcy.webtable.filter.*;
import top.hcy.webtable.router.Router;
import top.hcy.webtable.service.GetTableService;
import top.hcy.webtable.service.LoginService;
import top.hcy.webtable.service.WService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable
 * @ClassName: BootStrap
 * @Author: hcy
 * @Description: web table 处理入口
 * @Date: 2020/1/14 19:52
 * @Version: 1.0
 */
@Slf4j
public class BootStrap {

    //普通请求前置处理
    private WFiterChainImpl hPreRequest = null;

    private Reflections reflections = null;

    public BootStrap() {
        init();
    }

    private void init() {
        initReflections();
        initFilters();
        initDefaultAccount();
        initRouters();
        initKvData();
        saveInitializationKeys();
        initDefaultAccountPermission();
    }


    private void initDefaultAccountPermission() {
        ArrayList<String> baseKeys = WGlobal.baseKeys;
        String[][] defaultAccounts = WGlobal.DefaultAccounts;
        int length = defaultAccounts.length;
        int size =baseKeys.size();
        for (int i = 0; i < size; i++) {
            String key = baseKeys.get(i);
            for (int j = 0; j < length; j++) {
                kvDBUtils.copyKey(defaultAccounts[j][0]+"."+key,key);
            }
        }
        ArrayList<String> allKeys = kvDBUtils.getAllKeys();
       // System.out.println(allKeys);

    }


    private void saveInitializationKeys() {
        //储存读取到的 table 和 field 数据
        ArrayList<String> allKeys = kvDBUtils.getAllKeys();
        ArrayList<String> baseKeys = WGlobal.baseKeys;
        int size = allKeys.size();
        for (int i = 0; i < size; i++) {
            String s = allKeys.get(i);
            if (s.startsWith(WConstants.PREFIX_TABLE) || s.startsWith(WConstants.PREFIX_FIELD)){
                baseKeys.add(s);
            }
        }
    }

    private void initReflections() {
        reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(WGlobal.PACKAGE_SCAN) // 指定路径URL
//                .addScanners(new SubTypesScanner()) // 添加子类扫描工具
                .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
                .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
                .addScanners(new MethodParameterScanner() ) // 添加方法参数扫描工具
        );
    }

    private void initKvData() {
        saveTableData();
        saveFieldData();
        saveTriggerMethods();
        UpdateToShowMethods();
        UpdateToPersistenceMethods();

        JSONObject value = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD+"Data1"+"."+"name", WKVType.T_MAP);
        value = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD+"Data1"+"."+"age", WKVType.T_MAP);
    }

    private void UpdateToShowMethods() {
        Set<Method> wFieldToShowMethods = reflections.getMethodsAnnotatedWith(WFieldToShow.class);
        Iterator<Method> iterator = wFieldToShowMethods.iterator();
        while (iterator.hasNext()){
            Method method = iterator.next();
            WFieldToShow annotation = method.getAnnotation(WFieldToShow.class);
            String fieldName = annotation.value();
            String className = method.getDeclaringClass().getSimpleName();
            JSONObject field = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD+className+"."+fieldName, WKVType.T_MAP);
            if(field!=null){
                field.put("toShowMethod",method.getName());
                kvDBUtils.setValue(WConstants.PREFIX_FIELD+className+"."+fieldName,field, WKVType.T_MAP);
            }
        }
    }

    private void UpdateToPersistenceMethods() {
        Set<Method> wFieldToShowMethods = reflections.getMethodsAnnotatedWith(WFieldToPersistence.class);
        Iterator<Method> iterator = wFieldToShowMethods.iterator();
        while (iterator.hasNext()){
            Method method = iterator.next();
            WFieldToPersistence annotation = method.getAnnotation(WFieldToPersistence.class);
            String fieldName = annotation.value();
            String className = method.getDeclaringClass().getSimpleName();
            JSONObject field = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD+className+"."+fieldName, WKVType.T_MAP);
            if(field!=null) {
                field.put("toPersistenceMethod", method.getName());
                kvDBUtils.setValue(WConstants.PREFIX_FIELD + className + "." + fieldName, field, WKVType.T_MAP);
            }
        }
    }

    private void saveTriggerMethods() {
        updateTriggerMethods(WInsertTrigger.class,"insertTrigger");
        updateTriggerMethods(WUpdateField.class,"updateTrigger");
        updateTriggerMethods(WDeleteTrigger.class,"deleteTrigger");
        updateTriggerMethods(WSelectTrigger.class,"selectTrigger");
        //测试打印
//        JSONObject value = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + "Data1", WKVType.T_MAP);
//        System.out.println(value);
    }

    private void updateTriggerMethods(Class triggerClass,String key) {
        Set<Method> wInsertTriggerMethods = reflections.getMethodsAnnotatedWith(triggerClass);
        Iterator<Method> iterator = wInsertTriggerMethods.iterator();
        while (iterator.hasNext()){
            Method method = iterator.next();
            String className = method.getDeclaringClass().getSimpleName();
            JSONObject table = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + className, WKVType.T_MAP);
            table.put(key,method.getName());
            kvDBUtils.setValue(WConstants.PREFIX_TABLE+className,table, WKVType.T_MAP);
        }
    }

    private void saveFieldData() {
        Set<Field> fields = reflections.getFieldsAnnotatedWith(WField.class);
        Iterator<Field> iterator = fields.iterator();
        while (iterator.hasNext()){
            Field field = iterator.next();
            WField wField = field.getAnnotation(WField.class);
            String className = field.getDeclaringClass().getSimpleName();
            String intactClass = field.getDeclaringClass().getName();
            String fieldName = field.getName();
            String aliasFieldName = "".equals(wField.aliasName())?fieldName:wField.aliasName();
            String columnName = "".equals(wField.columnName())?fieldName:wField.columnName();
            String webFieldType = wField.fieldType().getStr();
            //表权限表
            ArrayList<String> fieldPermission = new ArrayList<>();
            if(field.getAnnotation(WInsertField.class)!=null ||
                    wField.insert() ){
                fieldPermission.add("insert");
            }
            if(field.getAnnotation(WUpdateField.class)!=null ||
                    wField.update() ){
                fieldPermission.add("update");
            }
            HashMap<String, Object> fieldData = new HashMap<>();
            fieldData.put("field",fieldName);
            fieldData.put("type",field.getType());
            fieldData.put("webFieldType",webFieldType);
            fieldData.put("class",className);
            fieldData.put("intactClass",intactClass);
            fieldData.put("alias",aliasFieldName);
            fieldData.put("column",columnName);
            fieldData.put("fieldPermission",fieldPermission);
            fieldData.put("toShowMethod",null);
            fieldData.put("toPersistenceMethod",null);
            kvDBUtils.setValue(WConstants.PREFIX_FIELD+className+"."+fieldName,fieldData, WKVType.T_MAP);
            JSONObject value = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD+className+"."+fieldName, WKVType.T_MAP);
         //   System.out.println(value);
        }
    }

    private void saveTableData() {
        Set<Class<?>> wTableClasses = reflections.getTypesAnnotatedWith(WTable.class);
        Iterator<Class<?>> iterator = wTableClasses.iterator();
        while (iterator.hasNext()){
            Class<?> wTableClass = iterator.next();
            WTable wTable = wTableClass.getAnnotation(WTable.class);
            String wTableClassName = wTableClass.getSimpleName();
            String aliasTableName = "".equals(wTable.aliasName())?wTableClassName:wTable.aliasName();
            String tableName = "".equals(wTable.tableName())?wTableClassName:wTable.tableName();
            //表权限表
            ArrayList<String> permission = new ArrayList<>();
            if(wTableClass.getAnnotation(WEnadbleDelete.class)!=null ||
                    wTable.delete() ){
                permission.add("delete");
            }
            if(wTableClass.getAnnotation(WEnadbleInsert.class)!=null ||
                    wTable.insert() ){
                permission.add("add");
            }
            if(wTableClass.getAnnotation(WEnadbleUpdate.class)!=null ||
                    wTable.update() ){
                permission.add("update");
            }
            ArrayList<String> f = new ArrayList<>();
            Field[] fields = wTableClass.getDeclaredFields();
            int length = fields.length;
            for (int i = 0; i < length; i++) {
                if ( fields[i].getAnnotation(WField.class)!=null){
                    f.add(fields[i].getName());
                }
            }

            HashMap<String, Object> tableData = new HashMap<>();
            tableData.put("table",tableName);
            tableData.put("alias",aliasTableName);
            tableData.put("class",wTableClassName);
            tableData.put("intactClass",wTableClass.getName());
            tableData.put("fields",f);
            tableData.put("permission",permission);
            tableData.put("insertTrigger",null);
            tableData.put("updateTrigger",null);
            tableData.put("selectTrigger",null);
            tableData.put("deleteTrigger",null);
            kvDBUtils.setValue(WConstants.PREFIX_TABLE+wTableClassName,tableData, WKVType.T_MAP);
          //  JSONObject value = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + wTableClassName, WKVType.T_MAP);
            WGlobal.tables.add(wTableClassName);
        }
    }

    private void initRouters() {
        Router.addRouter(WHandlerType.LoginRequest,new LoginService());
        Router.addRouter(WHandlerType.GTABLE,new GetTableService());
    }

    //处理入口
    public WResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);
        //检查url 和 请求方法
        hPreRequest.doFilter(ctx);
        if (ctx.isError()){
            return defulteWResponseEntity(ctx);
        }

        //处理 token key
        ctx.setUsername(ctx.getTokenKey());

        //获取对应service
        WService wService = ctx.getWService();
        //校验参数
        wService.verifyParams(ctx);
        if (ctx.isError()){
            return defulteWResponseEntity(ctx);
        }
        //执行service
        try {
            wService.doService(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("service error"+e.getClass().getName() +"  ctx:"+ ctx.toString());
            ctx.setWRespCode(WRespCode.REQUEST_SERVICE_ERROR);
        }
        HashMap res = new HashMap();
        if (ctx.isRefreshToken()){
            res.put("token",ctx.getNewToken());
        }else{
            res.put("token","");
        }

        res.put("data",ctx.getRespsonseEntity());

        return new WResponseEntity(ctx.getWRespCode(),res);
    }


    private void initDefaultAccount() {
        ArrayList<HashMap> list = new ArrayList<>();
        HashMap<String,String> data = null;
        String[][] defaultAccounts = WGlobal.DefaultAccounts;
        for (int i = 0; i < defaultAccounts.length; i++) {
            kvDBUtils.setValue(WConstants.PREFIX_ACCOUNTS +defaultAccounts[i][0],defaultAccounts[i][1], WKVType.T_STRING);
        }
    }

    public WResponseEntity defulteWResponseEntity(WebTableContext ctx){
        return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
    }

    private void initFilters() {
        RequestMethodFilter requestMethodFilter = new RequestMethodFilter();
        RequestUrlFilter requestUrlFilter = new RequestUrlFilter();
        TokenJwtFilter tokenJwtFilter = new TokenJwtFilter();
        hPreRequest = new WFiterChainImpl(WHandlerType.HPreRequest);
        hPreRequest.addFiterOnLast(requestMethodFilter);
        hPreRequest.addFiterOnLast(requestUrlFilter);
        hPreRequest.addFiterOnLast(tokenJwtFilter);
    }
}