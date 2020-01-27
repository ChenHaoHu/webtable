package top.hcy.webtable.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WebTable {
    String aliasName() default "";
    String tableName() default "";
    boolean read() default true;
    boolean insert() default false;
    boolean update() default false;
    boolean delete() default false;
}