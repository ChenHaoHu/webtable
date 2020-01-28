package top.hcy.webtable.common.enums;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.common
 * @ClassName: HandlerTypeCode
 * @Author: hcy
 * @Description: web table handler type
 * @Date: 2020/1/14 19:42
 * @Version: 1.0
 */
public enum WHandlerType {

    GFiled("","获取表详情"),
    HPreRequest("","检查request请求"),
    GTABLE("table","获取表数据信息"),
    LoginRequest("login","登录请求");

    private String uri;
    private String desc;

    WHandlerType(String uri,String desc) {
        this.uri = uri;
        this.desc = desc;
    }

    public String getUri() {
        return uri;
    }



    public String getDesc() {
        return desc;
    }

}
