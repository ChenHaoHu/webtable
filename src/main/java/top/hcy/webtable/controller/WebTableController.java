package top.hcy.webtable.controller;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hcy.webtable.WebTableBootStrap;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.wsql.kv.WKVType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;

@RestController
public class WebTableController {

    WebTableBootStrap webTabelHandler;
    public WebTableController() {

        //处理一些配置操作
        WGlobal.PACKAGE_ENTITY = "top.hcy.webtable.entity";

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("db.properties");
            Properties  p = new Properties();
            p.load(inputStream);
            DataSource dataSource = DruidDataSourceFactory.createDataSource(p);
            WGlobal.dataSource = dataSource;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 通过工厂类获取DataSource对象



        //初始化主类
        webTabelHandler = new WebTableBootStrap();

    }

    @RequestMapping("/webtable")
    public WResponseEntity getSome(HttpServletRequest reuqest, HttpServletResponse response){
        WResponseEntity handler = webTabelHandler.handler(reuqest, response);
        return handler;
    }


    @RequestMapping("/tmp/json")
    public  Object json(){
        ArrayList<String> allKeys = kvDBUtils.getAllKeys();
        HashMap<String,Object> data = new HashMap<>();
        for (int i = 0; i < allKeys.size(); i++) {
            data.put(allKeys.get(i), (String) kvDBUtils.getValue(allKeys.get(i), WKVType.T_STRING));

        };
        return data;
    }
}
