package top.hcy.webtable.db.mysql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @ProjectName: webtable
 * @Package: top.hcy.webtable.db.mysql
 * @ClassName: WSelectSql
 * @Author: hcy
 * @Description:
 * @Date: 20-1-26 22:11
 * @Version: 1.0
 **/
public class WSelectSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private int limit_x = 0;

    private int limit_y = 0;

    private boolean isLimit = false;

    private String condition  = "";

    public WSelectSql() {
    }

    public WSelectSql(String table) {
        this.table = table;
    }

    public WSelectSql table(String tableName){
        this.table = tableName;
        return this;
    }

    public WSelectSql fields(String... fields){
        Collections.addAll(this.fields,fields);
        return this;
    }

    public WSelectSql limit(int limit, int offset){
        isLimit = true;
        this.limit_x = limit;
        this.limit_y = offset;
        return this;
    }

    public WSelectSql page(int pageNum, int pageSize){
        isLimit = true;
        this.limit_x = pageSize;
        this.limit_y = pageNum*pageSize;
        return this;
    }

    public WSelectSql where(String condition){
        this.condition = condition;
        return this;
    }

    public ArrayList<HashMap<String,Object>> executeQuery(String... values){

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            sql.append(fields.get(i));
            if (i !=size-1){
                sql.append(",");
            }
        }

        sql.append(" FROM "+table);
        if (!condition.isEmpty()){
            sql.append(" WHERE "+condition+" ");
        }
        if (isLimit){
            sql.append(" LIMIT "+limit_x + "  OFFSET "+limit_y);
        }
        System.out.println(sql);
        ArrayList<HashMap<String, Object>> data = MySqlDbUtils.find(sql.toString(), values);
        return data;
    }

}