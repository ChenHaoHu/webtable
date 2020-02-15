package top.hcy.webtable.service.handle;

import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.enums.WHandlerType;
import top.hcy.webtable.router.UserRouterManagemanet;
import top.hcy.webtable.service.WService;

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
@Slf4j
@WHandleService(WHandlerType.USERINFO)
public class UserInfoService implements WService {
    @Override
    public void verifyParams(WebTableContext ctx) {

    }

    @Override
    public void doService(WebTableContext ctx) {
        HashMap<String,String> map = new HashMap<>();

        map.put("name",ctx.getUsername());
        map.put("avatar",ctx.getToken());
        map.put("routers", UserRouterManagemanet.generateRouters(ctx));

        log.info(UserRouterManagemanet.generateRouters(ctx));
        ctx.setRespsonseEntity(map);
    }
}