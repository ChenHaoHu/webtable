package top.hcy.webtable.filter;

import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.common.enums.WRespCode;

import javax.servlet.http.HttpServletRequest;


public class RequestMethodFilter implements WHandleFilter {
    @Override
    public void doFilter(WebTableContext ctx) {
        String method = "POST";
        HttpServletRequest request = ctx.getRequest();
        String requestMethod = request.getMethod();
        if(requestMethod!=null && method.equals(requestMethod.toUpperCase())){
        }else{
            ctx.setWRespCode(WRespCode.REQUEST_METHOD_ERROR);
            ctx.setError(true);
        }
    }
}
