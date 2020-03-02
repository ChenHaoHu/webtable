package top.hcy.webtable.logs;

import com.alibaba.fastjson.JSONArray;
import top.hcy.webtable.common.WebTableContext;


public interface WLogs {
    void info( WebTableContext ctx, String str);
    void warn( WebTableContext ctx, String str);
    void error(WebTableContext ctx, String str);
    JSONArray getLogs(String user, String role, Long start, Long end, Integer level);
}
