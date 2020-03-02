package top.hcy.webtable.common.constant;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;


public class WConstants {


    public static final int  PAGE_SIZE = 6;
    public static final int  PAGE_NUM = 0;
    public static final String TOKEN_SPLIT = ".wetable.";
    public static final String URI_NAME = "u";
    public static final String PREFIX_PK = "pk_";
    public static final String PREFIX_LOG = "log.";
    public static final String PREFIX_ACCOUNTS = "account.";
    public static final String PREFIX_TABLE = "table.";
    public static final String PREFIX_FIELD = "field.";
    public static final String[] NO_TOKEN_URI = new String[]{
            "/login","login",
    };

}
