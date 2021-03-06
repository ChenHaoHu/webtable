package top.hcy.webtable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.util.ConfigurationBuilder;
import top.hcy.webtable.annotation.charts.WChart;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.annotation.field.*;
import top.hcy.webtable.annotation.method.WDeleteTrigger;
import top.hcy.webtable.annotation.method.WInsertTrigger;
import top.hcy.webtable.annotation.method.WSelectTrigger;
import top.hcy.webtable.annotation.method.WUpdateTrigger;
import top.hcy.webtable.annotation.table.*;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.wsql.kv.WKVType;
import top.hcy.webtable.filter.*;
import top.hcy.webtable.service.Router;
import top.hcy.webtable.service.*;
import top.hcy.webtable.tools.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;
import static top.hcy.webtable.common.constant.WGlobal.wLogger;

@Slf4j
public class WebTableBootStrap {

    //普通请求前置处理
    private WFiterChainImpl hPreRequest = null;

    private Reflections reflections = null;

    public WebTableBootStrap() {
        init();
    }

    private void init() {
        initReflections();
        initFilters();
        initDefaultAccount();
        initHandleRouters();
        initKvData();
        saveInitializationKeys();
        initDefaultAccountPermission();
    }


    //处理入口
    public WResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);

        //设置threadlocal
        WGlobal.ctxThreadLocal.set(ctx);

        //设置请求时间
        ctx.setRequestTime(System.currentTimeMillis());

        //获取请求IP
        String ipAddr = CommonUtils.getIRealIPAddr(request);
        ctx.setIp(ipAddr);


        //检查url 和 请求方法
        hPreRequest.doFilter(ctx);
        if (ctx.isError()){
            return responseWResponseEntity(ctx);
        }

        //处理 token key
        String tokenKey = ctx.getTokenKey();
        if (tokenKey!=null){
            String[] split = tokenKey.split(WConstants.TOKEN_SPLIT);
            if (split.length == 2){
                ctx.setUsername(split[0]);
                ctx.setRole(split[1]);
            }else{
                ctx.setWRespCode(WRespCode.REQUEST_TOKEN_ERROR);
                return responseWResponseEntity(ctx);
            }
        }

        //获取对应service
        WService wService = ctx.getWService();
        //校验参数
        wService.verifyParams(ctx);
        if (ctx.isError()){
            wLogger.warn(ctx);
            return responseWResponseEntity(ctx);
        }
        //执行service
        try {
            wService.doService(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("service error"+e.getClass().getName() +"  ctx:"+ ctx.toString());
            ctx.setWRespCode(WRespCode.REQUEST_SERVICE_ERROR);
            wLogger.error(ctx);
        }finally {
        }

        HashMap res = new HashMap();
        if (ctx.isRefreshToken()){
            res.put("token",ctx.getNewToken());
        }else{
            res.put("token","");
        }

        res.put("data",ctx.getRespsonseEntity());

        if (ctx.isError()){
            wLogger.warn(ctx);
        }else{
            wLogger.info(ctx);
        }
        ctx.setRespsonseEntity(res);
        return responseWResponseEntity(ctx);
    }



    private void initHandleRouters() {
        Reflections re = new Reflections(WGlobal.HandleRoutersScanPackage);
        Set<Class<?>> cs = re.getTypesAnnotatedWith(WHandleService.class);
        Iterator<Class<?>> iterator = cs.iterator();
        ArrayList<String> logWhiteList = new ArrayList<>();

        try {
            while (iterator.hasNext()){
                Class<?> next = iterator.next();
                //   Class<?>[] interfaces = next.getInterfaces();
                Class<?> superclass = next.getSuperclass();
                boolean flag = false;
//                int length = interfaces.length;
//                for (int i = 0; i < length; i++) {
//                    if (WService.class.equals(interfaces[i])){
//                        flag = true;
//                        break;
//                    }
//                }
                if (WService.class.equals(superclass)){
                    flag = true;
                }
                if(flag){
                    WHandleService annotation = next.getAnnotation(WHandleService.class);
                    WHandlerType value = annotation.value();
                    logWhiteList.add(value.getUri());

                    Router.addRouter(value,(WService) next.newInstance());



                }else{
                    log.warn("initHandleRouters: class "+next.getName() +"is not implements WService");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            WGlobal.logWhiteList = logWhiteList;
        }
        //  new HandleRoutersManagement().invoke();
    }


    public void setDataSource(DataSource dataSource){
        WGlobal.dataSource = dataSource;
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

        for (int i = 0; i < length; i++) {
            //储存表目录
            kvDBUtils.copyKey(defaultAccounts[i][0]+"."+"tables","tables");
        }
    }


    private void saveInitializationKeys() {
        //储存读取到的 table 和 field 数据
        ArrayList<String> allKeys = kvDBUtils.getAllKeys();
        ArrayList<String> baseKeys = WGlobal.baseKeys;
        int size = allKeys.size();
        for (int i = 0; i < size; i++) {
            String s = allKeys.get(i);
//            if (s.startsWith(WConstants.PREFIX_TABLE) || s.startsWith(WConstants.PREFIX_FIELD)){
            if (s.startsWith(WConstants.PREFIX_TABLE)){
                baseKeys.add(s);
            }
        }
    }

    private void initReflections() {
        reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(WGlobal.PACKAGE_ENTITY) // 指定路径URL
//                .addScanners(new SubTypesScanner()) // 添加子类扫描工具
                .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
                .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
                .addScanners(new MethodParameterScanner() ) // 添加方法参数扫描工具
        );
    }

    private void initKvData() {
        saveTableData();
        saveFieldData();
        saveMethodsConfig();
        UpdateToShowMethods();
        UpdateToPersistenceMethods();

//        JSONObject value = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD+"Data1"+"."+"name", WKVType.T_MAP);
//        value = (JSONObject)kvDBUtils.getValue( WConstants.PREFIX_FIELD+"Data1"+"."+"age", WKVType.T_MAP);
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

    private void saveMethodsConfig() {
        updateMethodsConfig(WInsertTrigger.class,"insertTrigger");
        updateMethodsConfig(WUpdateTrigger.class,"updateTrigger");
        updateMethodsConfig(WDeleteTrigger.class,"deleteTrigger");
        updateMethodsConfig(WSelectTrigger.class,"selectTrigger");

        updateWChartConfig("wchart");
        updateAbstractFields(WAbstractField.class,"abstractfields");

    }

    private void updateWChartConfig( String key) {
        Set<Method> wtriggerMethods = reflections.getMethodsAnnotatedWith(WChart.class);
        Iterator<Method> iterator = wtriggerMethods.iterator();
        while (iterator.hasNext()){
            Method method = iterator.next();
            String className = method.getDeclaringClass().getSimpleName();
            JSONObject table = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + className, WKVType.T_MAP);
            if(table != null){
                JSONObject wcharts = table.getJSONObject(key);
                if (wcharts == null){
                    wcharts = new JSONObject();
                }
                WChart annotation = method.getAnnotation(WChart.class);
                String value = annotation.value();
                JSONObject item = new JSONObject();
                item.put("method",method.getName());
                item.put("showDashboard",annotation.showDashboard());
                wcharts.put(value,item);
                table.put(key,wcharts);
                kvDBUtils.setValue(WConstants.PREFIX_TABLE+className,table, WKVType.T_MAP);
            }
        }

    }

    public void closeResources(){
        WGlobal.ctxThreadLocal.remove();
    }

    private void updateAbstractFields(Class wAbstractFieldClass, String key) {
        Set<Method> wtriggerMethods = reflections.getMethodsAnnotatedWith(wAbstractFieldClass);
        Iterator<Method> iterator = wtriggerMethods.iterator();
        while (iterator.hasNext()){
            Method method = iterator.next();
            String className = method.getDeclaringClass().getSimpleName();
            JSONObject table = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + className, WKVType.T_MAP);
            if (table!=null){
                JSONArray wAbstractFields =  (JSONArray)table.get(key);
                if(wAbstractFields == null){
                    wAbstractFields = new JSONArray();
                }
                wAbstractFields.add(method.getName());
                table.put(key,wAbstractFields);
                kvDBUtils.setValue(WConstants.PREFIX_TABLE+className,table, WKVType.T_MAP);
            }
        }
    }

    private void updateMethodsConfig(Class triggerClass,String key) {
        Set<Method> wtriggerMethods = reflections.getMethodsAnnotatedWith(triggerClass);
        Iterator<Method> iterator = wtriggerMethods.iterator();
        while (iterator.hasNext()){
            Method method = iterator.next();
            String className = method.getDeclaringClass().getSimpleName();
            JSONObject table = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + className, WKVType.T_MAP);
            if (table!=null){
                table.put(key,method.getName());
                kvDBUtils.setValue(WConstants.PREFIX_TABLE+className,table, WKVType.T_MAP);
            }
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
            if(field.getAnnotation(WReadField.class)!=null ||
                    wField.read() ){
                fieldPermission.add("read");
            }
            if(field.getAnnotation(WInsertField.class)!=null ||
                    wField.insert() ){
                fieldPermission.add("insert");
            }
            if(field.getAnnotation(WUpdateField.class)!=null ||
                    wField.update() ){
                fieldPermission.add("update");
            }
            if(field.getAnnotation(WFindField.class)!=null ||
                    wField.find() ){
                fieldPermission.add("find");
            }
            if(field.getAnnotation(WSortField.class)!=null ||
                    wField.sort() ){
                fieldPermission.add("sort");
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


            //添加多选
            WSelectsField wSelectsField = field.getAnnotation(WSelectsField.class);
            if(wSelectsField !=null){
                String[] selects = wSelectsField.selects();
                fieldData.put("selects",selects);
            }

            kvDBUtils.setValue(WConstants.PREFIX_FIELD+className+"."+fieldName,fieldData, WKVType.T_MAP);
        }
    }

    private void saveTableData() {

        kvDBUtils.deleKey("tables");

        Set<Class<?>> wTableClasses = reflections.getTypesAnnotatedWith(WTable.class);
        Iterator<Class<?>> iterator = wTableClasses.iterator();
        while (iterator.hasNext()){
            Class<?> wTableClass = iterator.next();
            WTable wTable = wTableClass.getAnnotation(WTable.class);
            String wTableClassName = wTableClass.getSimpleName();
            String aliasTableName = "".equals(wTable.aliasName())?wTableClassName:wTable.aliasName();
            String tableName = "".equals(wTable.tableName())?wTableClassName:wTable.tableName();

            //储存表目录
            JSONArray tables = (JSONArray)kvDBUtils.getValue("tables", WKVType.T_LIST);
            if (tables == null){
                tables = new JSONArray();
            }
            tables.add(wTableClassName);
            kvDBUtils.setValue("tables",tables, WKVType.T_LIST);


            //表权限表
            ArrayList<String> permission = new ArrayList<>();
            if(wTableClass.getAnnotation(WEnadbleDelete.class)!=null ||
                    wTable.delete() ){
                permission.add("delete");
            }
            if(wTableClass.getAnnotation(WEnadbleInsert.class)!=null ||
                    wTable.insert() ){
                permission.add("insert");
            }
            if(wTableClass.getAnnotation(WEnadbleUpdate.class)!=null ||
                    wTable.update() ){
                permission.add("update");
            }
            if(wTableClass.getAnnotation(WEnadbleFind.class)!=null ||
                    wTable.find() ){
                permission.add("find");
            }
            if(wTableClass.getAnnotation(WEnadbleSort.class)!=null ||
                    wTable.sort() ){
                permission.add("sort");
            }
            if(wTableClass.getAnnotation(WEnadbleChart.class)!=null ||
                    wTable.chart() ){
                permission.add("chart");
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
            tableData.put("abstractfields",null);
            tableData.put("permission",permission);
            tableData.put("insertTrigger",wTable.insertTrigger());
            tableData.put("updateTrigger",wTable.updateTrigger());
            tableData.put("selectTrigger",wTable.selectTrigger());
            tableData.put("deleteTrigger",wTable.deleteTrigger());
            //可视化图表
            tableData.put("wchart",null);
            kvDBUtils.setValue(WConstants.PREFIX_TABLE+wTableClassName,tableData, WKVType.T_MAP);
            //  JSONObject value = (JSONObject)kvDBUtils.getValue(WConstants.PREFIX_TABLE + wTableClassName, WKVType.T_MAP);
            WGlobal.tables.add(wTableClassName);
        }
    }




    private void initDefaultAccount() {
        ArrayList<HashMap> list = new ArrayList<>();
        HashMap<String,String> data = null;
        String[][] defaultAccounts = WGlobal.DefaultAccounts;
        for (int i = 0; i < defaultAccounts.length; i++) {
            kvDBUtils.setValue(WConstants.PREFIX_ACCOUNTS +defaultAccounts[i][0],defaultAccounts[i][1], WKVType.T_STRING);
        }
    }

    public WResponseEntity responseWResponseEntity(WebTableContext ctx){
        closeResources();
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
