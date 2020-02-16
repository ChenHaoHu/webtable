package top.hcy.webtable.annotation.common;

import top.hcy.webtable.router.WHandlerType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WHandleService {
    WHandlerType value();
}
