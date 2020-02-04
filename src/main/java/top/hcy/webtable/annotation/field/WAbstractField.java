package top.hcy.webtable.annotation.field;

import top.hcy.webtable.common.enums.WebFieldType;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WAbstractField {
    String aliasName() default "";
    WebFieldType fieldType() default WebFieldType.STRING;
}