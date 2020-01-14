package top.hcy.webtable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.core.JsonStreamContext;
import top.hcy.webtable.common.RespCode;
import top.hcy.webtable.common.ResponseEntity;
import top.hcy.webtable.common.WebTableContext;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Enumeration;

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
        checkUrl(ctx,request);
        if (ctx.isError()){
            return new ResponseEntity(ctx.getRespCode(),ctx.getRespsonseEntity());
        }
        checkRequestMethod(ctx,request);
        if (ctx.isError()){
            return new ResponseEntity(ctx.getRespCode(),ctx.getRespsonseEntity());
        }
        paserParam(ctx,request);
        if (ctx.isError()){
            return new ResponseEntity(ctx.getRespCode(),ctx.getRespsonseEntity());
        }
        checkToken(ctx,request);
        if (ctx.isError()){
            return new ResponseEntity(ctx.getRespCode(),ctx.getRespsonseEntity());
        }
        return new ResponseEntity(ctx.getRespCode(),ctx.getRespsonseEntity());
    }



    //解析请求参数
    public void paserParam(WebTableContext ctx, HttpServletRequest request){
        try {

            ServletInputStream inputStream = request.getInputStream();
            int len  = 100;
            int b = 0;
            byte[] bytes = new byte[len];
            StringBuffer str = new StringBuffer();
            while ((b = inputStream.read(bytes))!=-1){
                str.append(new String(bytes).substring(0,b));
            }
            JSONObject jsonObject = JSON.parseObject(str.toString());
            System.out.println(jsonObject.getJSONArray("fields"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (!parameterMap.containsKey("type")){
//            ctx.setRespCode(RespCode.REQUEST_PARAM_WARN);
//            return;
//        }
//        System.out.println(parameterMap.get("type"));
    }

    //检查token
    private void checkToken(WebTableContext ctx, HttpServletRequest request) {
    }

    //检查请求方法
    private void checkRequestMethod(WebTableContext ctx, HttpServletRequest request) {
        String method = "POST";
        String requestMethod = request.getMethod();
        if(requestMethod!=null && method.equals(requestMethod.toString().toUpperCase())){
        }else{
            ctx.setRespCode(RespCode.REQUEST_METHOD_ERROR);
            ctx.setError(true);
        }
    }

    //处理url解析成分
    //uri : /webtable
    private void checkUrl(WebTableContext ctx,HttpServletRequest request){
        String uri = request.getRequestURI();
        String[] items = uri.split("/");
        int len = items.length;
        //不属于本项目处理内容
        if (len<=1  || (len>=2 && !("webtable".equals(items[1].toLowerCase())))){
            ctx.setRespCode(RespCode.REQUEST_URI_ERROR);
            ctx.setError(true);
        }
    }



}
