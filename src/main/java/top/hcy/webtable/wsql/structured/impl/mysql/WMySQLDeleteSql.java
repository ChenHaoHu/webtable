package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.wsql.structured.WDeleteSql;

import java.util.ArrayList;
import java.util.Collections;


@Slf4j
public class WMySQLDeleteSql implements WDeleteSql {

    private String table = "";

    private StringBuffer condition  = new StringBuffer();


    private ArrayList<String> conditionValues = new ArrayList<>();
    public WMySQLDeleteSql() {
    }

    public WMySQLDeleteSql(String table) {
        this.table = table;
    }

    @Override
    public WMySQLDeleteSql table(String tableName){
        this.table = tableName;
        return this;
    }

    @Override
    public WMySQLDeleteSql where(){
        if (this.condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }


//    @Override
//    public WMySQLDeleteSql where(String condition){
//
//        if (this.condition.length() == 0){
//            this.condition.append(condition);
//        }
//        return this;
//    }

    @Override
    public WMySQLDeleteSql and(String andField,String andValue){

        if (condition.length() != 0){
            this.condition.append("and "+andField+"=? ");
            this.conditionValues.add(andValue);
        }
        return this;
    }

    @Override
    public WMySQLDeleteSql or(String orField,String orValue){
        if (condition.length() != 0){
            this.condition.append("or "+orField+"=? ");
            this.conditionValues.add(orValue);
        }
        return this;
    }


    @Override
    public int executeDelete(){

        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM  "+table);

        if (condition.length()!=0){
            sql.append(" WHERE "+condition+" ");
        }

        log.info("sql: "+sql);
        log.info("values: "+ JSON.toJSONString(conditionValues));


        int i = MySQLDBUtils.delete(sql.toString(), conditionValues);
        return i;
    }

    @Override
    public String getSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM  "+table);

        if (condition.length()!=0){
            sql.append(" WHERE "+condition);
        }
        return sql.toString();
    }
}