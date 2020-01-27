package top.hcy.webtable.db.mysql;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WDeleteSqlTest {

    @Test
    void executeDelete() {
        WDeleteSql deleteSql = new WDeleteSql();
        int i = deleteSql.table("ttt")
                .where("id<?")
                .executeDelete("100");
        String sql = deleteSql.getSql();
        Assert.assertEquals(sql,"DELETE FROM  ttt WHERE id<?");
    }
}