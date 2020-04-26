package top.hcy.webtable.wsql.kv;


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
