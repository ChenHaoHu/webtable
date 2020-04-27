package top.hcy.webtable.wsql.structured.impl.mysql;

import top.hcy.webtable.wsql.structured.WTableData;

import java.util.ArrayList;


public class WMySQLTableData implements WTableData {

    private  String table = "";

    public WMySQLTableData() {
    }

    public WMySQLTableData(String table) {
        this.table = table;
    }

    @Override
    public WMySQLTableData table(String table){
        this.table = table;
        return this;
    }

    @Override
    public ArrayList<String> getPrimayKey(){
       ArrayList<String> primayKeys = MySQLDBUtils.getPrimayKey(table);
       return primayKeys;
   }
}