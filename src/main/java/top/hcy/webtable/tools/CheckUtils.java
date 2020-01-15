package top.hcy.webtable.tools;

import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.common.WebTableContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.tools
 * @ClassName: CheckUtil
 * @Author: hcy
 * @Description: check util
 * @Date: 2020/1/15 10:57
 * @Version: 1.0
 */
public class CheckUtils {
    //检查token
    public static void checkToken(WebTableContext ctx, HttpServletRequest request) {
    }

    //检查请求方法
    public static void checkRequestMethod(WebTableContext ctx, HttpServletRequest request) {
        String method = "POST";
        String requestMethod = request.getMethod();
        if(requestMethod!=null && method.equals(requestMethod.toString().toUpperCase())){
        }else{
            ctx.setWRespCode(WRespCode.REQUEST_METHOD_ERROR);
            ctx.setError(true);
        }
    }

    //处理url解析成分
    //uri : /webtable
    public static void checkUrl(WebTableContext ctx,HttpServletRequest request){
        String uri = request.getRequestURI();
        String[] items = uri.split("/");
        int len = items.length;
        //不属于本项目处理内容
        if (len<=1  || (len>=2 && !("webtable".equals(items[1].toLowerCase())))){
            ctx.setWRespCode(WRespCode.REQUEST_URI_ERROR);
            ctx.setError(true);
        }
    }
}
