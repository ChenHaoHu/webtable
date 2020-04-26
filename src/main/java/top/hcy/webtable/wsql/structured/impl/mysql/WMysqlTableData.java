package top.hcy.webtable.wsql.structured.impl.mysql;

import top.hcy.webtable.wsql.structured.WTableData;

import java.util.ArrayList;


public class WMysqlTableData implements WTableData {

    private  String table = "";

    public WMysqlTableData() {
    }

    public WMysqlTableData(String table) {
        this.table = table;
    }

    @Override
    public WMysqlTableData table(String table){
        this.table = table;
        return this;
    }

    @Override
    public ArrayList<String> getPrimayKey(){
       ArrayList<String> primayKeys = MySqlDBUtils.getPrimayKey(table);
       return primayKeys;
   }
}