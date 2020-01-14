package top.hcy.webtable;

import top.hcy.webtable.common.ResponseEntity;
import top.hcy.webtable.common.WebTableContext;

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
public class WebTabelHandler {

    //处理入口
    public ResponseEntity handler(HttpServletRequest request, HttpServletResponse response){
        WebTableContext ctx = new WebTableContext(request,response);

        return new ResponseEntity(ctx.getRespCode(),ctx.getRespsonseEntity());
    }



    public void PaserUrl(WebTableContext ctx){

    }

}
