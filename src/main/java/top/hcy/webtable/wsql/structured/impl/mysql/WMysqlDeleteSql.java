package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.wsql.structured.WDeleteSql;


@Slf4j
public class WMysqlDeleteSql implements WDeleteSql {

    private String table = "";

    private StringBuffer condition  = new StringBuffer();

    public WMysqlDeleteSql() {
    }

    public WMysqlDeleteSql(String table) {
        this.table = table;
    }

    @Override
    public WMysqlDeleteSql table(String tableName){
        this.table = tableName;
        return this;
    }

    @Override
    public WMysqlDeleteSql where(){
        if (this.condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }


    @Override
    public WMysqlDeleteSql where(String condition){

        if (this.condition.length() == 0){
            this.condition.append(condition);
        }
        return this;
    }

    @Override
    public WMysqlDeleteSql and(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"=? ");
        }
        return this;
    }

    @Override
    public WMysqlDeleteSql or(String orStr){
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


        int i = MySqlDBUtils.delete(sql.toString(), values);
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