package top.hcy.webtable.db.kv;

/**
 * @ClassName: WKVType
 * @Author: hcy
 * @Description:
 * @Date: 2020-01-20 23:51
 * @Version: 1.0
 **/
public enum WKVType {

    T_STRING("string"),
    T_LIST("list"),
    T_MAP("map");

    private String str;

    WKVType(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
