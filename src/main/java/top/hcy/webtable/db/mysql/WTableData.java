package top.hcy.webtable.db.mysql;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.db.mysql
 * @ClassName: WTableData
 * @Author: hcy
 * @Description:
 * @Date: 20-1-28 15:30
 * @Version: 1.0
 **/
public class WTableData {

    private  String table = "";

    public WTableData() {
    }

    public WTableData(String table) {
        this.table = table;
    }

    WTableData  table(String table){
        this.table = table;
        return this;
    }

   ArrayList<String> getPrimayKey(){
       ArrayList<String> primayKeys = MySqlDbUtils.getPrimayKey(table);
       return primayKeys;
   }
}