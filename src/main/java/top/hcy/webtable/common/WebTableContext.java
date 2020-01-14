package top.hcy.webtable.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    private HandlerTypeCode handlerTypeCode;
    private HttpServletRequest reuqest;
    private  HttpServletResponse response;
    private  Object respsonseEntity = null;
    private String tableName;
    private String[] fieldsName;
    private RespCode respCode = RespCode.SUCCESS;

    public WebTableContext(HttpServletRequest reuqest, HttpServletResponse response) {
        this.reuqest = reuqest;
        this.response = response;
    }

    private WebTableContext() {
    }
}
