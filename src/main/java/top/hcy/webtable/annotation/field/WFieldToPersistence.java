package top.hcy.webtable.annotation.field;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WFieldToPersistence {
    String value() default  "";
}
