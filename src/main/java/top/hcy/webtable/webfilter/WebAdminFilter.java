package top.hcy.webtable.webfilter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
        String fileName = "" + requestURI;
        fileName = fileName.replace("/", File.separator);
        fileName = fileName.replace("\\", File.separator);
        fileName = fileName.substring(1);
        System.out.println(fileName);
        PrintWriter writer = servletResponse.getWriter();
        System.out.println(requestURI);
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String tempString = null;
        while ((tempString = br.readLine()) != null) {
            System.out.println(tempString.getBytes("UTF-8"));
            writer.print(tempString);
        }
        writer.flush();
    }

    @Override
    public void destroy() {

    }
}