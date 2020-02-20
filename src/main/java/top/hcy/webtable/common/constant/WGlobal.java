package top.hcy.webtable.common.constant;

import top.hcy.webtable.db.kv.WKvDbUtils;
import top.hcy.webtable.db.kv.LevelDBUtils;

import javax.sql.DataSource;
import java.util.ArrayList;


public class WGlobal {

    public static DataSource dataSource = null;

    public static  ArrayList<String> tables = new ArrayList<>();

    public static WKvDbUtils kvDBUtils = new LevelDBUtils();

    public static ArrayList<String> baseKeys =  new ArrayList<>();

    public static  final String[][] DefaultAccounts  = {
            {"secadmin","admin"},
            {"admin","admin"}
    };


    public static  final String[] TOKEN_POWER = new String[]{
            "hcytoken"
    };

    public static final long TOKEN_REFRESH_TIME = 20 * 1000;

    public static  String PACKAGE_ENTITY = "top.hcy.webtable.entity";

    public static  String HandleRoutersScanPackage = "top.hcy.webtable.service.handle";

    public static final String JWT_SECRET = "eyJhbGciOiJIUzUxMiJ9";

    public static  final String[] WRoles  = {
            "admin","share"
    };


}
