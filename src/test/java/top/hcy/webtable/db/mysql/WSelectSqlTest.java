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
                .where("author = ?")
                .limit(2,0)
                .executeQuery("胡晨阳");
        String s = JSON.toJSONString(d);
        System.out.println(s);
    }
}