package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.wsql.structured.WUpdateSql;

import java.util.ArrayList;


@Slf4j
public class WMySQLUpdateSql implements WUpdateSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private ArrayList<String> values = new ArrayList<>();

    private StringBuffer condition  = new StringBuffer();

    public WMySQLUpdateSql() {
    }

    public WMySQLUpdateSql(String table) {
        this.table = table;
    }

    @Override
    public WMySQLUpdateSql table(String tableName){
        this.table = tableName;
        return this;
    }

    @Override
    public WMySQLUpdateSql update(String field, String value){
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

    @Override
    public WMySQLUpdateSql where(){
        if (condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }

    @Override
    public WMySQLUpdateSql where(String condition){

        if (this.condition.length() == 0){
            this.condition.append(condition);
        }
        return this;
    }

    @Override
    public WMySQLUpdateSql and(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"=? ");
        }
        return this;
    }

    @Override
    public WMySQLUpdateSql or(String orStr){
        if (condition.length() != 0){
            this.condition.append("or "+orStr+"=? ");
        }
        return this;
    }

    @Override
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

        int insert = MySQLDBUtils.update(sql.toString(),values,conditionValues);

        return insert;
    }


    @Override
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