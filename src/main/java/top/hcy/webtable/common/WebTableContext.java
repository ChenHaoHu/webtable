package top.hcy.webtable.common;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.ToString;
import top.hcy.webtable.common.enums.WRespCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.common
 * @ClassName: WebTableContext
 * @Author: hcy
 * @Description: webtable http context
 * @Date: 2020/1/14 19:41
 * @Version: 1.0
 */
@Data
@ToString
public class WebTableContext {

    private top.hcy.webtable.common.enums.WHandlerTypeCode WHandlerTypeCode;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object respsonseEntity = null;
    private String tableName;
    private String[] fieldsName;
    private WRespCode wRespCode = WRespCode.SUCCESS;
    private boolean error;
    private JSONObject params;
    private String username;
    private String token;
    private String tokenKey;
    private String realUri;
    private String newToken;

    public WebTableContext(HttpServletRequest reuqest, HttpServletResponse response) {
        this.request = reuqest;
        this.response = response;
    }

    private WebTableContext() {
    }
}
