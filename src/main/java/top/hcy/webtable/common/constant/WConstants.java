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

    public static final String PACKAGE_SCAN = "top.hcy.webtable.entity";


    public static final String JWT_SECRET = "eyJhbGciOiJIUzUxMiJ9";

    public static final String URI_NAME = "u";

    public static final String PREFIX_ACCOUNTS = "account.";

    public static final long TOKEN_REFRESH_TIME = 20 * 1000;

    public static  final String[] NO_TOKEN_URI = new String[]{
            "/login","login",
    };

    public static  final String[] TOKEN_POWER = new String[]{
            "hcytoken"
    };

    public static  final String[][] DefaultAccounts  = {
            {"secadmin","admin"},
            {"admin","admin"}
    };


}
