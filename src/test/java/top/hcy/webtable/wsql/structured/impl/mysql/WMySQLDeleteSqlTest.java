package top.hcy.webtable.wsql.structured.impl.mysql;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class WMySQLDeleteSqlTest {

    @Test
    void executeDelete() {
        WMySQLDeleteSql deleteSql = new WMySQLDeleteSql();
       deleteSql.table("ttt")
                .where("id<?");
//                .executeDelete("100");
        String sql = deleteSql.getSql();
        Assert.assertEquals(sql,"DELETE FROM  ttt WHERE id<?");
    }
}