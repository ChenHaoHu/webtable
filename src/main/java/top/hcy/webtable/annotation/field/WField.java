package top.hcy.webtable.annotation.field;

import top.hcy.webtable.common.enums.WebFieldType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WField {
    String aliasName() default "";
    String columnName() default "";
    WebFieldType fieldType() default WebFieldType.STRING;
    boolean read() default true;
    boolean insertField() default false;
    boolean updateField() default false;
    String convertMethod() default "";
}