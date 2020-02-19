package top.hcy.webtable;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import top.hcy.webtable.webfilter.WebAdminFilter;

@ConditionalOnWebApplication
public class WebAdminFilterConfiguration {
    @Bean
    public FilterRegistrationBean webStatFilterRegistrationBean( ) {


        System.out.println("WebAdminFilterConfiguration ");
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebAdminFilter());
        registration.addUrlPatterns("/wadmin/*");
        registration.addInitParameter("wadmin", "wadmin");
        registration.setName("wadmin");
//        registration.setOrder(0);
        return registration;
    }
}