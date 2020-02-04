package top.hcy.webtable.db.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.db.mysql
 * @ClassName: WDeleteSql
 * @Author: hcy
 * @Description:
 * @Date: 20-1-27 14:14
 * @Version: 1.0
 **/
@Slf4j
public class WDeleteSql {

    private String table = "";

    private StringBuffer condition  = new StringBuffer();

    public WDeleteSql() {
    }

    public WDeleteSql(String table) {
        this.table = table;
    }

    public WDeleteSql table(String tableName){
        this.table = tableName;
        return this;
    }

    public WDeleteSql where(){
        if (this.condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }


    public WDeleteSql where(String condition){

        if (this.condition.length() == 0){
            this.condition.append(condition);
        }
        return this;
    }

    public WDeleteSql and(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"=? ");
        }
        return this;
    }

    public WDeleteSql or(String orStr){
        if (condition.length() != 0){
            this.condition.append("or "+orStr+"=? ");
        }
        return this;
    }


    public int executeDelete(String... values){

        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM  "+table);

        if (condition.length()!=0){
            sql.append(" WHERE "+condition+" ");
        }

        log.info("sql: "+sql);
        log.info("values: "+ JSON.toJSONString(values));


        int i = MySqlDbUtils.delete(sql.toString(), values);
        return i;
    }

    public String getSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM  "+table);

        if (condition.length()!=0){
            sql.append(" WHERE "+condition);
        }
        return sql.toString();
    }
}