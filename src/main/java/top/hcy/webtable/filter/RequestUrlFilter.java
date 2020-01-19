package top.hcy.webtable.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.bcel.classfile.Constant;
import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.tools.ParamUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: RequestUrlFilter
 * @Author: hcy
 * @Description:
 * @Date: 2020-01-19 23:35
 * @Version: 1.0
 **/
public class RequestUrlFilter implements WHandlerFilter {
    @Override
    public void doFilter(WebTableContext ctx) {
        //处理url解析成分
        //uri : /webtable
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        String[] items = uri.split("/");
        int len = items.length;
        //不属于本项目处理内容
        if (len<=1  || (len>=2 && !("webtable".equals(items[1].toLowerCase())))){
            ctx.setWRespCode(WRespCode.REQUEST_URI_ERROR);
            ctx.setError(true);
        }

        //获取请求的form
        ParamUtils.getParamsFromRequest(ctx,request);
        JSONObject params = ctx.getParams();

        boolean b = params.containsKey(WConstants.URI_NAME);
        if(b){
            String u = (String) params.get(WConstants.URI_NAME);
            if(u.isEmpty()){
                ctx.setWRespCode(WRespCode.REQUEST_URI_EMPTY);
                ctx.setError(true);
            }
            ctx.setRealUri(u);
        }else{
            ctx.setWRespCode(WRespCode.REQUEST_URI_LOST);
            ctx.setError(true);
        }


    }
}
