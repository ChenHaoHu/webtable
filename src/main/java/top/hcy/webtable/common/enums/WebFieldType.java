package top.hcy.webtable.common.enums;


public enum WebFieldType {

    STRING("String"),
    IMAGEURL("ImageURL"),
    NUMBER("Number"),
    SELECT("Select"),
    IMAGEBASE64("ImageBASE64");


    private String str;

    WebFieldType(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}