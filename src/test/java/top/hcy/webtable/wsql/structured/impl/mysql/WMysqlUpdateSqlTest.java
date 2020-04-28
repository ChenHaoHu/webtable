package top.hcy.webtable.wsql.structured.impl.mysql;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class WMysqlUpdateSqlTest {

    @Test
    void executeUpdate() {
        WMySQLUpdateSql updateSql = new WMySQLUpdateSql();
//        updateSql.table("ttt")
//                .update("email", "100000")
//                .where("id < ?")
//                .executeUpdate("101");
//        String sql = updateSql.getSql();
//        Assert.assertEquals(sql,"UPDATE  ttt SET  email = ? WHERE id < ? ");
    }
}