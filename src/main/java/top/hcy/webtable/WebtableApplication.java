package top.hcy.webtable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class WebtableApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebtableApplication.class, args);
    }

}
