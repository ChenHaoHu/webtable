package top.hcy.webtable.service;

import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.filter.WFilterChain;
import top.hcy.webtable.service.WService;

import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.router
 * @ClassName: Router
 * @Author: hcy
 * @Description: 请求转发表
 * @Date: 20-1-25 19:31
 * @Version: 1.0
 **/
public class Router {

    private static HashMap<String, WService> routers = new HashMap<>();

    public static void addRouter(WHandlerType u, WService s){
        routers.put(u.getUri(),s);
    }

    public static WService getService(String u){
        //这里做大小写 带斜杠处理
        u = u.trim();
        u = u.toLowerCase();
        if (u.startsWith("/")){
            u.substring(1);
        }
        WService wService = routers.get(u);
        return wService;
    }

}