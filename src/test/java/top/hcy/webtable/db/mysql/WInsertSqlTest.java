package top.hcy.webtable.db.mysql;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WInsertSqlTest {

    @Test
    void execute() {
        WInsertSql insertSql = new WInsertSql();
         insertSql = insertSql.table("ttt")
                .fields("id", "email", "date")
                .values("110", "775656764@qq.com", "2018-01-02 12:22")
                .values("111", "775656764@qq.com", "2018-01-02 12:22");
        String sql = insertSql.getSql();
        Assert.assertEquals(sql,"INSERT INTO ttt (id,email,date)VALUES(?,?,?),(?,?,?)");
    }
}