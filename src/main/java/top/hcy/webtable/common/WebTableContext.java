package top.hcy.webtable.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.ToString;
import top.hcy.webtable.common.enums.WRespCode;
import top.hcy.webtable.service.WService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


@Data
@ToString
public class WebTableContext {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object respsonseEntity = null;
    private String tableName;
    private String[] fieldsName;
    private WRespCode wRespCode = WRespCode.SUCCESS;
    private boolean error;
    private JSONObject params;
    private String username;
    private String role;
    private String token;
    private String tokenKey;
    private String realUri;
    private String newToken;
    private String ip;
    // 管理员权限表 非分享权限
    private JSONArray permissions;
    //记录执行的sql
    private JSONArray executedSQLs;
    private long requestTime;
    private long responseTime;
    private boolean refreshToken = false;
    private WService wService;

    public WebTableContext(HttpServletRequest reuqest, HttpServletResponse response) {
        this.request = reuqest;
        this.response = response;
    }

    private WebTableContext() {
    }

    @Override
    public String toString() {
        return "WebTableContext{" +
                "request=" + request +
                ", response=" + response +
                ", respsonseEntity=" + respsonseEntity +
                ", tableName='" + tableName + '\'' +
                ", fieldsName=" + Arrays.toString(fieldsName) +
                ", wRespCode=" + wRespCode +
                ", error=" + error +
                ", params=" + params +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", tokenKey='" + tokenKey + '\'' +
                ", realUri='" + realUri + '\'' +
                ", newToken='" + newToken + '\'' +
                ", refreshToken=" + refreshToken +
                ", wService=" + wService +
                '}';
    }
}
