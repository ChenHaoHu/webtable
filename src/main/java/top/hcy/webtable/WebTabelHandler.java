package top.hcy.webtable;

import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.response.WResponseEntity;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.tools.CheckUtils;
import top.hcy.webtable.tools.ParamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

    public WebTabelHandler() {

    }

    //处理入口
    public WResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);
        CheckUtils.checkUrl(ctx,request);
        if (ctx.isError()){
            return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
        }
        CheckUtils.checkRequestMethod(ctx,request);
        if (ctx.isError()){
            return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
        }
        ParamUtils.getParamsFromRequest(ctx,request);
        if (ctx.isError()){
            return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
        }
        CheckUtils.checkToken(ctx,request);
        if (ctx.isError()){
            return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
        }
        return new WResponseEntity(ctx.getWRespCode(),ctx.getRespsonseEntity());
    }
}
