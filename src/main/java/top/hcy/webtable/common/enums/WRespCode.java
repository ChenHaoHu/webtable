package top.hcy.webtable.common.enums;

public enum WRespCode {
    SUCCESS(1000, "请求成功"),
    ERROR(1001,"请求失败"),
    WARN(1002, "请求错误"),
    REQUEST_URI_ERROR(1003, "URI错误"),
    REQUEST_METHOD_ERROR(1004, "请求方法错误"),
    REQUEST_URI_LOST(1005, "请求体丢失实际请求uri"),
    REQUEST_URI_EMPTY(1006, "请求体实际请求uri为空"),
    REQUEST_PARAM_ERROR(1013, "请求参数不合法"),
    REQUEST_TOKEN_ERROR(1007, "TOKEN 无效"),
    REQUEST_TOKEN_LOST(1008, "TOKEN 缺失"),
    REQUEST_CHAIN_LOST(1009, "请求为空"),
    REQUEST_SERVICE_ERROR(1010, "请求处理失败"),
    LOGIN_SUCCESS(1011, "登录成功"),
    LOGIN_FAILE(1012, "登录失败，账号或密码错误"),
    TABLE_NULL(1013, "没有可操作的数据表"),
    PERMISSION_DENIED(1014, "没有操作权限"),
    FIELD_UNFAMILIAR(1015, "表字段不明确"),
    PK_UNFAMILIAR(1015, "主键表字段不明确"),
    UPDATE_NODATA(1016, "没有数据更新"),
    UPDATE_SUCCESS(1017, "更新成功"),
    UPDATE_FAIL(1018, "更新失败"),
    INSERT_SUCCESS(1019, "插入成功"),
    INSERT_FAIL(1020, "插入失败"),
    DELETE_SUCCESS(1021, "删除成功"),
    DELETE_FAIL(1022, "删除失败"),
    DELETE_NODATA(1023, "没有数据删除"),
    LOGIN_SHAREFORBID(1024, "此分享账号临时封停"),
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