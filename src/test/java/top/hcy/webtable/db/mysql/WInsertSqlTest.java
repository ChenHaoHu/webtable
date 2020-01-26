package top.hcy.webtable.db.mysql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WInsertSqlTest {

    @Test
    void execute() {
        WInsertSql insertSql = new WInsertSql();
        int execute = insertSql.table("ttt")
                .fields("id", "email","date")
                .values("110", "775656764@qq.com","2018-01-02 12:22")
                .execute();
        System.out.println(execute);
    }
}