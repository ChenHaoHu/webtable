package top.hcy.webtable;

import top.hcy.webtable.common.HandlerTypeCode;
import top.hcy.webtable.common.RespCode;
import top.hcy.webtable.common.ResponseEntity;
import top.hcy.webtable.common.WebTableContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable
 * @ClassName: WebTabelHandler
 * @Author: hcy
 * @Description: web table 处理入口
 * @Date: 2020/1/14 19:52
 * @Version: 1.0
 */
public class WebTabelHandler {

    //处理入口
    public ResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);
        paserUrl(ctx);
        return new ResponseEntity(ctx.getRespCode(),ctx.getRespsonseEntity());
    }



    //处理url解析成分
    //uri : /webtable/[type]/[table]/[field]
    private void paserUrl(WebTableContext ctx){
        HttpServletRequest reuqest = ctx.getReuqest();/**/
        String uri = reuqest.getRequestURI();
        String[] items = uri.split("/");
        int len = items.length;
        //不属于本项目处理内容
        if (len<=1  || (len>=2 && "webtable".equals(items[1].toString().toLowerCase()))){
            ctx.setRespCode(RespCode.URI_ERROR);
        }
        //解析处理类型
        if (len >= 4){
            HandlerTypeCode handlerTypeCode =HandlerTypeCode.valueOf(items[2].toUpperCase());
            if(handlerTypeCode!=null){
                ctx.setHandlerTypeCode(handlerTypeCode);
                ctx.setTableName(items[3].toUpperCase());
            }
        }
        if(len>=5){
            String[] fields = Arrays.copyOf(items, 5);
            ctx.setFieldsName(fields);
        }

    //    ctx.setRespsonseEntity(ctx);
    }

}
