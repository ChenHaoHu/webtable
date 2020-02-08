package top.hcy.webtable.annotation.table;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WTable {
    String aliasName() default "";
    String tableName() default "";
    boolean read() default true;
    boolean find() default true;
    boolean insert() default false;
    boolean update() default false;
    boolean delete() default false;
    String selectTrigger() default "";
    String insertTrigger() default "";
    String updateTrigger() default "";
    String deleteTrigger() default "";
}