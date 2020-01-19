package top.hcy.webtable;

import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.enums.WHandlerTypeCode;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.filter.RequestMethodFilter;
import top.hcy.webtable.filter.RequestUrlFilter;
import top.hcy.webtable.filter.TokenJwtFilter;
import top.hcy.webtable.filter.WFiterChainImpl;
import top.hcy.webtable.tools.ParamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable
 * @ClassName: WebTabelHandler
 * @Author: hcy
 * @Description: web table 处理入口
 * @Date: 2020/1/14 19:52
 * @Version: 1.0
 */
@Slf4j
public class WebTabelHandler {

    WFiterChainImpl hPreRequest = null;



    public WebTabelHandler() {
        RequestMethodFilter requestMethodFilter = new RequestMethodFilter();
        RequestUrlFilter requestUrlFilter = new RequestUrlFilter();
        TokenJwtFilter tokenJwtFilter = new TokenJwtFilter();
        hPreRequest = new WFiterChainImpl(WHandlerTypeCode.HPreRequest);
        hPreRequest.addFileterOnLast(requestMethodFilter);
        hPreRequest.addFileterOnLast(requestUrlFilter);
        hPreRequest.addFileterOnLast(tokenJwtFilter);
    }

    //处理入口
    public WResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);
        //检查url 和 请求方法
        hPreRequest.doFilter(ctx);
        if (ctx.isError()){
          return defulteWResponseEntity(ctx);
        }





        return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
    }

    public WResponseEntity defulteWResponseEntity(WebTableContext ctx){
        return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
    }
}
