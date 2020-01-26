package top.hcy.webtable.db.mysql;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WSelectSqlTest {

    @Test
    void executeQuery() {
        WSelectSql selectSql = new WSelectSql();
        ArrayList<HashMap<String, Object>> d;
        d = selectSql
                .table("task")
                .fields("task_id","author", "task_name")
                .where("task_id < ?")
                .limit(2,3)
                .executeQuery("15");
        String s = JSON.toJSONString(d);
        System.out.println(s);
    }
}