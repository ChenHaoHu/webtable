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


    public static final int  PAGE_SIZE = 6;
    public static final int  PAGE_NUM = 0;

    public static final String URI_NAME = "u";
    public static final String PREFIX_PK = "pk_";
    public static final String PREFIX_ACCOUNTS = "account.";
    public static final String PREFIX_TABLE = "table.";
    public static final String PREFIX_FIELD = "field.";
    public static  final String[] NO_TOKEN_URI = new String[]{
            "/login","login",
    };

}
