package top.hcy.webtable.db.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class WUpdateSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private ArrayList<String> values = new ArrayList<>();

    private StringBuffer condition  = new StringBuffer();

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

    public WUpdateSql where(){
        if (condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }

    public WUpdateSql where(String condition){

        if (this.condition.length() == 0){
            this.condition.append(condition);
        }
        return this;
    }

    public WUpdateSql and(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"=? ");
        }
        return this;
    }

    public WUpdateSql or(String orStr){
        if (condition.length() != 0){
            this.condition.append("or "+orStr+"=? ");
        }
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

        if (condition.length()!=0){
            sql.append(" WHERE "+condition+" ");
        }

        log.info("sql: "+sql);
        log.info("values: "+ JSON.toJSONString(values));
        log.info("conditionValues: "+JSON.toJSONString(conditionValues));

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
        if (condition.length()!= 0){
            sql.append(" WHERE "+condition+" ");
        }
        return sql.toString();
    }
}