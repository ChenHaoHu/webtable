package top.hcy.webtable.db.mysql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.db.mysql
 * @ClassName: WInsertSql
 * @Author: hcy
 * @Description:
 * @Date: 20-1-27 00:44
 * @Version: 1.0
 **/
public class WInsertSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private ArrayList<ArrayList<String>> values = new ArrayList<>();

    public WInsertSql() {
    }

    public WInsertSql(String table) {
        this.table = table;
    }

    public WInsertSql table(String tableName){
        this.table = tableName;
        return this;
    }

    public WInsertSql fields(String... fields){
        Collections.addAll(this.fields,fields);
        return this;
    }

    public WInsertSql values(String... values){
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,values);
        this.values.add(list);
        return this;
    }

    public int executeInsert(){

        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO "+table +" (");
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            sql.append(fields.get(i));
            if (i !=size-1){
                sql.append(",");
            }
        }
        sql.append(")VALUES");
        size = values.size();
        for (int i = 0; i < size; i++) {
            sql.append("(");
            ArrayList<String> value = values.get(i);
            int valueSize = value.size();
            for (int j = 0; j < valueSize; j++) {
                sql.append("?");
                if (j !=valueSize-1){
                    sql.append(",");
                }
            }
            sql.append(")");
            if (i !=size-1){
                sql.append(",");
            }
        }
        int insert = MySqlDbUtils.insert(sql.toString(),values);
        return insert;
    }

    public String getSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO "+table +" (");
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            sql.append(fields.get(i));
            if (i !=size-1){
                sql.append(",");
            }
        }
        sql.append(")VALUES");
        size = values.size();
        for (int i = 0; i < size; i++) {
            sql.append("(");
            ArrayList<String> value = values.get(i);
            int valueSize = value.size();
            for (int j = 0; j < valueSize; j++) {
                sql.append("?");
                if (j !=valueSize-1){
                    sql.append(",");
                }
            }
            sql.append(")");
            if (i !=size-1){
                sql.append(",");
            }
        }
        return sql.toString();
    }

}