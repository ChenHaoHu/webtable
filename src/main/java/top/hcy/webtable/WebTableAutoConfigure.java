package top.hcy.webtable;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.hcy.webtable.common.constant.WGlobal;

import javax.sql.DataSource;


@Configuration
@ConditionalOnClass(WebTableBootStrap.class)
@EnableConfigurationProperties(StarterServiceProperties.class)
@Import({WebTableFilterConfiguration.class})
@Slf4j
public class WebTableAutoConfigure {

    @Autowired
    private StarterServiceProperties properties;

    @Autowired
    private ApplicationContext appContext;

    @Bean()
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "webtable", value = "enabled", havingValue = "true")
    WebTableBootStrap webTableBootStrap (){
        String entityPack = properties.getEntitypack();
        WGlobal.PACKAGE_ENTITY = entityPack;
        log.info("webtable will scan package: "+ entityPack);
        DataSource bean = appContext.getBean(DataSource.class);
        if (bean==null){
            log.error("datasource can not be null. you should init DataSource");
            return null;
        }
        log.info("webatble will use datasource: " + bean.getClass());
        WebTableBootStrap webTableBootStrap = new WebTableBootStrap();
        webTableBootStrap.setDataSource(bean);
        return webTableBootStrap;
    }


}