package top.hcy.webtable.service;

import com.alibaba.fastjson.JSONObject;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.constant.WGlobal;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.db.kv.WKVType;
import top.hcy.webtable.tools.JwtTokenUtils;

import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: LoginService
 * @Author: hcy
 * @Description: 登录处理
 * @Date: 20-1-25 22:07
 * @Version: 1.0
 **/
public class UserInfoService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        HashMap<String,String> map = new HashMap<>();

        map.put("name",ctx.getUsername());
        map.put("avatar",ctx.getToken());
        map.put("routers",UserRouterManagemanet.generateRouters(ctx));

        System.out.println(UserRouterManagemanet.generateRouters(ctx));
        ctx.setRespsonseEntity(map);
    }
}