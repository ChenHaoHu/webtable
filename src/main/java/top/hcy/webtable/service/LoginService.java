package top.hcy.webtable.service;

import top.hcy.webtable.common.WebTableContext;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.service
 * @ClassName: LoginService
 * @Author: hcy
 * @Description: 登录处理
 * @Date: 20-1-25 22:07
 * @Version: 1.0
 **/
public class LoginService implements WService {
    @Override
    public void doService(WebTableContext ctx) {
        System.out.println("---------------");
        ctx.setRespsonseEntity("-----");
    }
}