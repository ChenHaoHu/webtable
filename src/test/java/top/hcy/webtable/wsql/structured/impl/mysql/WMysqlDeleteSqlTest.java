package top.hcy.webtable.wsql.structured.impl.mysql;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class WMysqlDeleteSqlTest {

    @Test
    void executeDelete() {
        WMysqlDeleteSql deleteSql = new WMysqlDeleteSql();
       deleteSql.table("ttt")
                .where("id<?");
//                .executeDelete("100");
        String sql = deleteSql.getSql();
        Assert.assertEquals(sql,"DELETE FROM  ttt WHERE id<?");
    }
}