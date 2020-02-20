package top.hcy.webtable.webfilter;


import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.util.Utils;
import top.hcy.webtable.WebTableBootStrap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

public class WebAdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        String requestURI = request.getRequestURI();
        String filePath = "" + requestURI;
        filePath = filePath.substring(1);

        if (!filePath.contains(".")){
            if (filePath.endsWith("/")){
                filePath = filePath + "index.html";
            }else{
                filePath = filePath + "/index.html";
            }
        }
        returnResourceFile(response, filePath);

        // System.out.println(filePath);
        //InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        // System.out.println(Thread.currentThread().getContextClassLoader().getResource(filePath));
        //URL resource = loader.getResource("");
//        System.out.println(resource);
//        System.out.println(loader.getResource(fileName));
//        fileName = resource.getPath()+fileName;
//        System.out.println(fileName);
//        InputStream resourceAsStream = loader.getResourceAsStream(fileName);
        //       BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
//        InputStreamReader isr=new InputStreamReader(resourceAsStream,"utf8");
//        BufferedReader br=new BufferedReader(isr);
//        PrintWriter writer = servletResponse.getWriter();
//        String tempString = null;
//        while ((tempString = br.readLine()) != null) {
//            System.out.println(tempString);
//            writer.print(tempString);
//        }
//        writer.flush();
    }

    private void returnResourceFile(HttpServletResponse response, String filePath) throws IOException {
        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
        if (filePath.endsWith(".jpg") || filePath.endsWith(".woff")|| filePath.endsWith(".ttf") ) {
            byte[] bytes = Utils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }
        } else {
            String text = Utils.readFromResource(filePath);
            if (text == null) {
                response.setStatus(404);
            } else {
                if (filePath.endsWith(".css")) {
                    response.setContentType("text/css;charset=utf-8");
                } else if (filePath.endsWith(".js")) {
                    response.setContentType("text/javascript;charset=utf-8");
                }
                response.getWriter().write(text);
            }
        }
    }


    @Override
    public void destroy() {

    }
}