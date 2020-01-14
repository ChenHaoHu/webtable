package top.hcy.webtable.common;

public enum RespCode {
    SUCCESS(1000, "请求成功"),
    ERROR(1001,"请求失败"),
    WARN(1002, "请求错误"),
    URI_ERROR(1003, "URI转发错误");


    private int code;
    private String msg;

    RespCode(int code, String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}