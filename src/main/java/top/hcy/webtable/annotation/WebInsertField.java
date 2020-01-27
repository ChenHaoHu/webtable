package top.hcy.webtable.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WebInsertField {

}