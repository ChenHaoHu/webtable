package top.hcy.webtable.annotation.table;


import top.hcy.webtable.wsql.structured.factory.WSQLDBType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WDataSource {
    WSQLDBType DBtype = WSQLDBType.MYSQL;
    String name = "default";
}
