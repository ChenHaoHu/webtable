package top.hcy.webtable.db.mysql;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WTableDataTest {

    @Test
    void getPrimayKey() {
        WTableData wTableData = new WTableData();
        ArrayList<String> ttt = wTableData.table("ttt").getPrimayKey();
        System.out.println(ttt);
    }
}