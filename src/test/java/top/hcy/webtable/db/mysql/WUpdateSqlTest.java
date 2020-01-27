package top.hcy.webtable.db.mysql;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WUpdateSqlTest {

    @Test
    void executeUpdate() {
        WUpdateSql updateSql = new WUpdateSql();
        updateSql.table("ttt")
                .update("email", "100000")
                .where("id < ?")
                .executeUpdate("101");
        String sql = updateSql.getSql();
        Assert.assertEquals(sql,"UPDATE  ttt SET  email = ? WHERE id < ? ");
    }
}