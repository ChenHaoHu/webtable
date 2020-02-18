package top.hcy.webtable.router;

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
    GTABLE("gtable","获取表中数据信息"),
    UTABLE("utable","更新表中数据信息"),
    DTABLE("dtable","删除表中数据信息"),
    ATABLE("atable","添加表中数据信息"),
    GKVDATA("gkvdata","获取kv数据库信息"),
    USERINFO("userinfo","获取用户信息"),
    GSHAREPERMISSIONLIST("gsharepermissionlist","获取分享内容权限表"),
    GSHAREDATA("gshare","获取分享列表"),
    USHAREDATA("ushare","更新分享列表"),
    ASHAREDATA("ashare","添加分享信息"),
    DSHAREDATA("dshare","删除分享信息"),
    GCHART("gchart","获取设定的图表分析数据"),
    GHOMEPAGE("ghomepage","获取首页信息"),
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
