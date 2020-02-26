package top.hcy.webtable.webfilter;


import com.alibaba.fastjson.JSON;
import top.hcy.webtable.WebTableBootStrap;
import top.hcy.webtable.common.response.WResponseEntity;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebTableFilter implements Filter {


    WebTableBootStrap webTableBootStrap = new WebTableBootStrap();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        WResponseEntity handler = webTableBootStrap.handler(request, response);
        String s = JSON.toJSONString(handler);
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = servletResponse.getWriter();
        writer.print(s);
        writer.flush();
        writer.close();
    }

    @Override
    public void destroy() {

    }
}