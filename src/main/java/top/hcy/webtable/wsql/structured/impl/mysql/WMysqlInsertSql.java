package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.wsql.structured.WInsertSql;

import java.util.ArrayList;
import java.util.Collections;


@Slf4j
public class WMysqlInsertSql implements WInsertSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private ArrayList<ArrayList<String>> values = new ArrayList<>();

    public WMysqlInsertSql() {
    }

    public WMysqlInsertSql(String table) {
        this.table = table;
    }

    @Override
    public WMysqlInsertSql table(String tableName){
        this.table = tableName;
        return this;
    }

    @Override
    public WMysqlInsertSql fields(String... fields){
        Collections.addAll(this.fields,fields);
        return this;
    }

    @Override
    public WMysqlInsertSql values(String... values){
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,values);
        this.values.add(list);
        return this;
    }

    @Override
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
        log.info("sql: "+sql);
        log.info("values: "+ JSON.toJSONString(values));


        int insert = MySqlDBUtils.insert(sql.toString(),values);
        return insert;
    }

    @Override
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