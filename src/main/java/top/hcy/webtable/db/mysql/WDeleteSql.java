package top.hcy.webtable.db.mysql;

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
public class WDeleteSql {

    private String table = "";

    private String condition  = "";

    public WDeleteSql() {
    }

    public WDeleteSql(String table) {
        this.table = table;
    }

    public WDeleteSql table(String tableName){
        this.table = tableName;
        return this;
    }

    public WDeleteSql where(String condition){
        this.condition = condition;
        return this;
    }

    public int executeDelete(String... values){

        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM  "+table);

        if (!condition.isEmpty()){
            sql.append(" WHERE "+condition+" ");
        }

        int i = MySqlDbUtils.delete(sql.toString(), values);
        return i;
    }

    public String getSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM  "+table);

        if (!condition.isEmpty()){
            sql.append(" WHERE "+condition+" ");
        }
        return sql.toString();
    }
}