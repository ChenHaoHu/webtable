package top.hcy.webtable.common.enums;

public enum WRespCode {
    SUCCESS(1000, "请求成功"),
    ERROR(1001,"请求失败"),
    WARN(1002, "请求错误"),
    REQUEST_URI_ERROR(1003, "URI错误"),
    REQUEST_METHOD_ERROR(1004, "请求方法错误"),
    REQUEST_URI_LOST(1005, "请求体丢失实际请求uri"),
    REQUEST_URI_EMPTY(1006, "请求体实际请求uri为空"),
    REQUEST_TOKEN_ERROR(1007, "TOKEN 无效"),
    REQUEST_TOKEN_LOST(1008, "TOKEN 缺失"),
    REQUEST_PARAM_WARN(1100, "请求参数不合法");


    private int code;
    private String msg;

    WRespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}