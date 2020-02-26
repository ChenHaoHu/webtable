package top.hcy.webtable.service;

import top.hcy.webtable.router.WHandlerType;

import java.util.HashMap;


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