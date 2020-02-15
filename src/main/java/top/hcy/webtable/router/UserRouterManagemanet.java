package top.hcy.webtable.router;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.db.kv.WKVType;

import java.util.ArrayList;

import static top.hcy.webtable.common.constant.WGlobal.kvDBUtils;


/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: UserRouterManagemanet
 * @Author: hcy
 * @Description:
 * @Date: 20-2-2 15:28
 * @Version: 1.0
 **/
public class UserRouterManagemanet {

    //生成前端路由
    public static String generateRouters(WebTableContext ctx){

        JSONArray routers = new JSONArray();

        String username = ctx.getUsername();

        JSONArray tables = (JSONArray) kvDBUtils.getValue(username + ".tables", WKVType.T_LIST);

        int size = tables.size();
        for (int i = 0; i < size; i++) {
            String table = tables.getString(i);
            JSONObject tableData = (JSONObject)kvDBUtils.getValue(username + "." + WConstants.PREFIX_TABLE + table, WKVType.T_MAP);
            UserRouter userRouter = new UserRouter("table   ", table, "/table/index",tableData.getString("alias"),"table");
            JSONObject s = pathItemOneChildren("/"+table, userRouter);
            routers.add(s);
        }

        if ("admin".equals(ctx.getRole())){
            //wadmin 路由
            ArrayList<UserRouter> wadminUserRouters = new ArrayList<>();
            UserRouter permissionRouter = new UserRouter("permission","permission","/wadmin/permission/index","权限管理","form");
            UserRouter memberRouter = new UserRouter("member","member","/wadmin/member/index","账号管理","tree");
            UserRouter shareRouter = new UserRouter("share","share","/wadmin/share/index","分享管理","nested");
            wadminUserRouters.add(permissionRouter);
            wadminUserRouters.add(memberRouter);
            wadminUserRouters.add(shareRouter);
            JSONObject wadminRouters = pathItemWithChildren("/wadmin", "wadmin", "example", wadminUserRouters);
            routers.add(wadminRouters);
        }


        //默认添加 404
        JSONObject router404 = add404Router();
        routers.add(router404);
        return routers.toJSONString();
    }

    private static JSONObject add404Router() {
        JSONObject router = new JSONObject();
        router.put("path","*");
        router.put("redirect","/404");
        router.put("hidden",true);
        return router;
    }


    public static JSONObject pathItemWithChildren(String path, String title,String icon,ArrayList<UserRouter> userRouters){
        JSONObject router = new JSONObject();
        router.put("path",path);
        JSONArray childrens = new JSONArray();
        router.put("children",childrens);
        JSONObject meta = new JSONObject();
        meta.put("title",title);
        meta.put("icon",icon);
        router.put("meta",meta);
        for (int i = 0; i < userRouters.size(); i++) {
            childrens.add(userRouters.get(i));
        }
        return router;
    }

    public static JSONObject pathItemOneChildren(String path,UserRouter userRouter){
        JSONObject router = new JSONObject();
        router.put("path",path);
        JSONArray childrens = new JSONArray();
        router.put("children",childrens);
        childrens.add(userRouter);
        return router;
    }
}