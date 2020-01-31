package top.hcy.webtable.annotation.field;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WFindField {
}