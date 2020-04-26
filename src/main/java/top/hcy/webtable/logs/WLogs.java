package top.hcy.webtable.logs;

import com.alibaba.fastjson.JSONArray;
import top.hcy.webtable.common.WebTableContext;


public interface WLogs {
    void info( WebTableContext ctx);
    void warn( WebTableContext ctx);
    void error(WebTableContext ctx);
    JSONArray getLogs(String user, String role, Long start, Long end, Integer level);
}
