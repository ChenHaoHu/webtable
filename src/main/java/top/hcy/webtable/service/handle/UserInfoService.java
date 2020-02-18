package top.hcy.webtable.service.handle;

import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.annotation.common.WHandleService;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.router.UserRouterManagemanet;
import top.hcy.webtable.service.WService;

import java.util.HashMap;


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