package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.wsql.structured.WDeleteSql;


@Slf4j
public class WMySQLDeleteSql implements WDeleteSql {

    private String table = "";

    private StringBuffer condition  = new StringBuffer();

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


    @Override
    public WMySQLDeleteSql where(String condition){

        if (this.condition.length() == 0){
            this.condition.append(condition);
        }
        return this;
    }

    @Override
    public WMySQLDeleteSql and(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"=? ");
        }
        return this;
    }

    @Override
    public WMySQLDeleteSql or(String orStr){
        if (condition.length() != 0){
            this.condition.append("or "+orStr+"=? ");
        }
        return this;
    }


    @Override
    public int executeDelete(String... values){

        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM  "+table);

        if (condition.length()!=0){
            sql.append(" WHERE "+condition+" ");
        }

        log.info("sql: "+sql);
        log.info("values: "+ JSON.toJSONString(values));


        int i = MySQLDBUtils.delete(sql.toString(), values);
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