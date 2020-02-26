package top.hcy.webtable.db.mysql;

import java.util.ArrayList;
import java.util.List;


public class WTableData {

    private  String table = "";

    public WTableData() {
    }

    public WTableData(String table) {
        this.table = table;
    }

    public WTableData  table(String table){
        this.table = table;
        return this;
    }

    public ArrayList<String> getPrimayKey(){
       ArrayList<String> primayKeys = MySqlDbUtils.getPrimayKey(table);
       return primayKeys;
   }
}