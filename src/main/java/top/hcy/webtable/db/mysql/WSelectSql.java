package top.hcy.webtable.db.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.constant.WConstants;

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
@Slf4j
public class WSelectSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private int limit_x = 0;

    private int limit_y = 0;

    private boolean isLimit = false;

    private StringBuffer condition  = new StringBuffer();

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

    public WSelectSql fieldsPk(String pk){
        this.fields(pk+" as "+ WConstants.PREFIX_PK+pk);
        return this;
    }

    public WSelectSql count(){
        fields.clear();
        fields.add("count(1) as count");
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

    public WSelectSql where(){
        if (this.condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }


    public WSelectSql where(String condition){

        if (this.condition.length() == 0){
            this.condition.append(condition);
        }
        return this;
    }

    public WSelectSql and(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"=? ");
        }
        return this;
    }

    public WSelectSql or(String orStr){
        if (condition.length() != 0){
            this.condition.append("or "+orStr+"=? ");
        }
        return this;
    }

    public WSelectSql like(String likeStr){
        if (condition.length() != 0){
            this.condition.append("and "+likeStr+"  like ?");
        }
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
        if (condition.length() != 0){
            sql.append(" WHERE "+condition+" ");
        }
        if (isLimit){
            sql.append(" LIMIT "+limit_x + "  OFFSET "+limit_y);
        }
        log.info("sql: "+sql);
        log.info("values: "+ JSON.toJSONString(values));

        ArrayList<HashMap<String, Object>> data = MySqlDbUtils.find(sql.toString(), values);
        return data;
    }

    public String getSql() {
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
        if (condition.length() != 0){
            sql.append(" WHERE "+condition+" ");
        }
        if (isLimit){
            sql.append(" LIMIT "+limit_x + "  OFFSET "+limit_y);
        }
        return sql.toString();
    }
}