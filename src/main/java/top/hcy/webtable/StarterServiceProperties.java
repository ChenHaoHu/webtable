package top.hcy.webtable;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("webtable")
@Data
public class StarterServiceProperties {

   private String entitypack;

 }