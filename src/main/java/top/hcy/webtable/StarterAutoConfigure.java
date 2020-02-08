package top.hcy.webtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(WebTableBootStrap.class)
@EnableConfigurationProperties(StarterServiceProperties.class)
public class StarterAutoConfigure {

    @Autowired
    private StarterServiceProperties properties;


    @Bean()
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "webtable", value = "enabled", havingValue = "true")
    WebTableBootStrap starterService (){
        System.out.println(" ----- 7777 -----");
        System.out.println(properties);

        String entityPack = properties.getEntitypack();
        System.out.println(entityPack);

        return new WebTableBootStrap(entityPack);
    }
}