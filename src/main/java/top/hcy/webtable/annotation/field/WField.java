package top.hcy.webtable.annotation.field;

import top.hcy.webtable.common.enums.WebFieldType;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WField {
    String aliasName() default "";
    String columnName() default "";
    WebFieldType fieldType() default WebFieldType.STRING;
    boolean read() default true;
    boolean insert() default false;
    boolean update() default false;
    boolean find() default false;
    boolean sort() default false;
//    String convertToShow() default "";
//    String convertToPersistence() default "";
}