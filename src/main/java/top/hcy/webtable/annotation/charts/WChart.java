package top.hcy.webtable.annotation.charts;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WChart {
    String value() default "";
    boolean showDashboard() default false;
}
