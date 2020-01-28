package top.hcy.webtable.annotation.field;

import top.hcy.webtable.common.enums.WebFieldType;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WField {
    String aliasName() default "";
    String columnName() default "";
    WebFieldType fieldType() default WebFieldType.STRING;
    boolean insert() default false;
    boolean update() default false;
//    String convertToShow() default "";
//    String convertToPersistence() default "";
}