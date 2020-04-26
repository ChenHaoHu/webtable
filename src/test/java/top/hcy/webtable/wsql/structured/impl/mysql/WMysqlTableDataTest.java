package top.hcy.webtable.wsql.structured.impl.mysql;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class WMysqlTableDataTest {

    @Test
    void getPrimayKey() {
        WMysqlTableData wMysqlTableData = new WMysqlTableData();
        ArrayList<String> ttt = wMysqlTableData.table("ttt").getPrimayKey();
        System.out.println(ttt);
    }
}