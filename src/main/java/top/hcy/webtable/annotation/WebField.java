package top.hcy.webtable.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WebField {
    String aliasName() default "";
    String columnName() default "";
    boolean read() default true;
    boolean insertField() default false;
    boolean updateField() default false;
}