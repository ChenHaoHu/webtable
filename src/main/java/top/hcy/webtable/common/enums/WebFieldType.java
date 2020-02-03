package top.hcy.webtable.common.enums;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.common.enums
 * @ClassName: WebFieldType
 * @Author: hcy
 * @Description:
 * @Date: 20-1-27 23:43
 * @Version: 1.0
 **/
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