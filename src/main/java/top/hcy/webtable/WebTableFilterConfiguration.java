package top.hcy.webtable;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import top.hcy.webtable.webfilter.WebAdminFilter;
import top.hcy.webtable.webfilter.WebTableFilter;

@ConditionalOnWebApplication
public class WebTableFilterConfiguration {

    @Bean
    public FilterRegistrationBean webAdminFilterRegistrationBean( ) {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebAdminFilter());
        registration.addUrlPatterns("/wadmin/*");
        registration.addInitParameter("wadmin", "wadmin");
        registration.setName("wadmin");
//        registration.setOrder(0);
        return registration;
    }

    @Bean
    public FilterRegistrationBean webTableFilterRegistrationBean( ) {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebTableFilter());
        registration.addUrlPatterns("/webtable");
        registration.addInitParameter("webtable", "webtable");
        registration.setName("webtable");
//        registration.setOrder(0);
        return registration;
    }
}