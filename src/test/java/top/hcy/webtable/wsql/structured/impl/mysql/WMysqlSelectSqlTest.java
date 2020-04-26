package top.hcy.webtable.wsql.structured.impl.mysql;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

class WMysqlSelectSqlTest {

    @Test
    void executeQuery() {
        WMysqlSelectSql selectSql = new WMysqlSelectSql();
        ArrayList<HashMap<String, Object>> d;
        selectSql = selectSql
                .table("task")
                .fields("task_id","author", "task_name")
                .where("author = ?")
                .orderBy("task_id",true)
                .limit(2,0);
//                .executeQuery("胡晨阳");
        String sql = selectSql.getSql();
        System.out.println(sql);
        Assert.assertEquals(sql,"SELECT task_id,author,task_name FROM task WHERE author = ?  ORDER BY task_id DESC LIMIT 2  OFFSET 0");
    }
}