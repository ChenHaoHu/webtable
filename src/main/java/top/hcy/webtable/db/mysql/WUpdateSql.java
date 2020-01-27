package top.hcy.webtable.db.mysql;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.db.mysql
 * @ClassName: WUpdateSql
 * @Author: hcy
 * @Description:
 * @Date: 20-1-28 13:34
 * @Version: 1.0
 **/
public class WUpdateSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private ArrayList<String> values = new ArrayList<>();

    private String condition  = "";

    public WUpdateSql() {
    }

    public WUpdateSql(String table) {
        this.table = table;
    }

    public WUpdateSql table(String tableName){
        this.table = tableName;
        return this;
    }

    public WUpdateSql update(String field,String value){
        fields.add(field);
        values.add(value);
        return this;
    }

//    public WUpdateSql fields(String... fields){
//        Collections.addAll(this.fields,fields);
//        return this;
//    }
//
//    public WUpdateSql values(String... values){
//        ArrayList<String> list = new ArrayList<>();
//        Collections.addAll(list,values);
//        this.values.add(list);
//        return this;
//    }

    public WUpdateSql where(String condition){
        this.condition = condition;
        return this;
    }

    public int executeUpdate(String... conditionValues){

        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE  "+table +" SET  ");
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            sql.append(fields.get(i) +" = ?");
            if (i !=size-1){
                sql.append(",");
            }
        }
        if (!condition.isEmpty()){
            sql.append(" WHERE "+condition+" ");
        }
        int insert = MySqlDbUtils.update(sql.toString(),values,conditionValues);

        return insert;
    }


    public String getSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE  "+table +" SET  ");
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            sql.append(fields.get(i) +" = ?");
            if (i !=size-1){
                sql.append(",");
            }
        }
        if (!condition.isEmpty()){
            sql.append(" WHERE "+condition+" ");
        }
        return sql.toString();
    }
}