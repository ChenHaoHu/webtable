package top.hcy.webtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.hcy.webtable.common.constant.WGlobal;

import javax.sql.DataSource;


@Configuration
@ConditionalOnClass(WebTableBootStrap.class)
@EnableConfigurationProperties(StarterServiceProperties.class)
@Import({WebAdminFilterConfiguration.class})
public class StarterAutoConfigure {

    @Autowired
    private StarterServiceProperties properties;

    @Autowired
    private ApplicationContext appContext;

    @Bean()
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "webtable", value = "enabled", havingValue = "true")
    WebTableBootStrap starterService (){
        String entityPack = properties.getEntitypack();
        WGlobal.PACKAGE_ENTITY = entityPack;
        return new WebTableBootStrap();
    }


}