package top.hcy.webtable.common;

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
public class WebTableContext {

    private  WebTableHandlerTypeCode handlerTypeCode;
    private HttpServletRequest reuqest;
    private  HttpServletResponse response;
    private  Object respsonseEntity;
    private String tablename;
    private String[] filedsname;
    private RespCode respCode;

    public WebTableContext(HttpServletRequest reuqest, HttpServletResponse response) {
        this.reuqest = reuqest;
        this.response = response;
    }

    public WebTableHandlerTypeCode getHandlerTypeCode() {
        return handlerTypeCode;
    }

    public void setHandlerTypeCode(WebTableHandlerTypeCode handlerTypeCode) {
        this.handlerTypeCode = handlerTypeCode;
    }

    public HttpServletRequest getReuqest() {
        return reuqest;
    }

    public void setReuqest(HttpServletRequest reuqest) {
        this.reuqest = reuqest;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Object getRespsonseEntity() {
        return respsonseEntity;
    }

    public void setRespsonseEntity(Object respsonseEntity) {
        this.respsonseEntity = respsonseEntity;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String[] getFiledsname() {
        return filedsname;
    }

    public void setFiledsname(String[] filedsname) {
        this.filedsname = filedsname;
    }

    public RespCode getRespCode() {
        return respCode;
    }

    public void setRespCode(RespCode respCode) {
        this.respCode = respCode;
    }
}
