package top.hcy.webtable.common.constant;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.common
 * @ClassName: Constant
 * @Author: hcy
 * @Description: 常量对照表
 * @Date: 2020/1/14 21:00
 * @Version: 1.0
 */
public class WConstants {

    public static final String JWT_SECRET = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTExMSIsImV";

    public static final long JWT_EXPIRATION = 60 * 60 * 1000;

}
