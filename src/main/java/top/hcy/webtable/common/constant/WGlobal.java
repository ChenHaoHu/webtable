package top.hcy.webtable.common.constant;

import top.hcy.webtable.common.WebTableContext;
import top.hcy.webtable.router.WHandlerType;
import top.hcy.webtable.wsql.kv.WKvDbUtils;
import top.hcy.webtable.wsql.kv.LevelDBUtils;
import top.hcy.webtable.logs.WLogger;

import javax.sql.DataSource;
import java.util.ArrayList;


public class WGlobal {

    public static WLogger wLogger = new WLogger();

    public static DataSource dataSource = null;

    public static  ArrayList<String> tables = new ArrayList<>();

    public static String DB_FILE = "DB_WebTable";

    public static WKvDbUtils kvDBUtils = new LevelDBUtils();

    public static ArrayList<String> baseKeys =  new ArrayList<>();

    public static   String[][] DefaultAccounts  = {
            {"secadmin","admin"},
            {"admin","admin"}
    };


    public static   String[] TOKEN_POWER = new String[]{
            "hcytoken"
    };

    public static  long TOKEN_REFRESH_TIME = 20 * 1000;

    public static  String PACKAGE_ENTITY = "top.hcy.webtable.entity";

    public static  String HandleRoutersScanPackage = "top.hcy.webtable.service.handle";

    public static  String JWT_SECRET = "eyJhbGciOiJIUzUxMiJ9";

    public static  String[] WRoles  = {
            "admin","share"
    };

    public static ArrayList<String> logWhiteList ;

    public static ThreadLocal<WebTableContext> ctxThreadLocal = new ThreadLocal<>();

}
