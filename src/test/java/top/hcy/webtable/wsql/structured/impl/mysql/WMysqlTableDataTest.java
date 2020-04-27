package top.hcy.webtable.wsql.structured.impl.mysql;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class WMysqlTableDataTest {

    @Test
    void getPrimayKey() {
        WMySQLTableData wMysqlTableData = new WMySQLTableData();
        ArrayList<String> ttt = wMysqlTableData.table("ttt").getPrimayKey();
        System.out.println(ttt);
    }
}