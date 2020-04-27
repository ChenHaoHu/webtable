package top.hcy.webtable.wsql.structured.impl.mongo;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import top.hcy.webtable.wsql.structured.WInsertSql;

import static org.junit.jupiter.api.Assertions.*;

class WMongoInsertSqlTest {

    void initDB(){

    }


    @Test
    void executeInsert() {

        WMongoInsertSql wMongoInsertSql = new WMongoInsertSql();
        int i = wMongoInsertSql.table("test").fields("aa", "bb").values("1111", "22222")
                .executeInsert();
        Assert.assertEquals(i,0);
    }
}