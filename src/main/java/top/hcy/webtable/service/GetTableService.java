package top.hcy.webtable.service;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.util.ConfigurationBuilder;
import top.hcy.webtable.annotation.field.WField;
import top.hcy.webtable.annotation.field.WFieldToShow;
import top.hcy.webtable.annotation.table.WTable;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.db.mysql.WSelectSql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: GetTableService
 * @Author: hcy
 * @Description:
 * @Date: 20-1-28 22:16
 * @Version: 1.0
 **/
public class GetTableService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        // 扫包
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(WConstants.PACKAGE_SCAN) // 指定路径URL
//                .addScanners(new SubTypesScanner()) // 添加子类扫描工具
                .addScanners(new FieldAnnotationsScanner()) // 添加 属性注解扫描工具
                .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
                .addScanners(new MethodParameterScanner() ) // 添加方法参数扫描工具
        );
        Set<Class<?>> c = reflections.getTypesAnnotatedWith(WTable.class);
        HashMap<String,Object> res = new HashMap<>();
        Iterator<Class<?>> iterator = c.iterator();
        while (iterator.hasNext()){
            Class<?> next = iterator.next();
            WTable wTable = next.getDeclaredAnnotation(WTable.class);
            String aliasName = wTable.aliasName();
            String tableName = wTable.tableName();
            WSelectSql selectSql = new WSelectSql(tableName);
            Field[] fields = next.getDeclaredFields();
            int length = fields.length;
            for (int i = 0; i < length; i++) {
                WField annotation = fields[i].getAnnotation(WField.class);
                if (annotation!=null){
                    if ("".equals(annotation.columnName())){
                        selectSql.fields(fields[i].getName());
                    }else{
                        selectSql.fields(annotation.columnName());
                    }
                }
            }
            ArrayList<HashMap<String, Object>> data = selectSql.limit(10,0).executeQuery();
            ArrayList<Object> obs = new ArrayList<>();
            int size = data.size();
            for (int i = 0; i < size; i++) {
                HashMap<String, Object> map = data.get(i);
                Object o = null;
                try {
                    o = next.newInstance();
//                    for(String str: map.keySet()){
//                        Field f = next.getDeclaredField(str);
//                        f.setAccessible(true);
//                        f.set(o,map.get(str));
//                    }
                    for (int j = 0; j < length; j++) {
                        if (map.get(fields[j].getName())!=null){
                            fields[j].setAccessible(true);
                            fields[j].set(o,map.get(fields[j].getName()));
                        }else{

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Set<Method> m = reflections.getMethodsAnnotatedWith(WFieldToShow.class);
                Iterator<Method> iterator1 = m.iterator();
                if (iterator1.hasNext()){
                    Method next1 = iterator1.next();
                }


                obs.add(o);
            }
            Method[] methods = next.getDeclaredMethods();

            res.put(aliasName,obs);
        }
        ctx.setRespsonseEntity(res);
    }
}