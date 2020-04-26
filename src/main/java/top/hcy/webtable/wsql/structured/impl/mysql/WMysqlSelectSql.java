package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.wsql.structured.WSelectSql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


@Slf4j
public class WMysqlSelectSql implements WSelectSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private int limit_x = 0;

    private int limit_y = 0;

    private boolean isLimit = false;

    private StringBuffer condition  = new StringBuffer();

    private String orderByField = null;

    private boolean orderByDesc = false;

    public WMysqlSelectSql() {
    }

    public WMysqlSelectSql(String table) {
        this.table = table;
    }

    @Override
    public WMysqlSelectSql table(String tableName){
        this.table = tableName;
        return this;
    }

    @Override
    public WMysqlSelectSql fields(String... fields){
        Collections.addAll(this.fields,fields);
        return this;
    }

    @Override
    public WMysqlSelectSql fieldsPk(String pk){
        this.fields(pk+" as "+ WConstants.PREFIX_PK+pk);
        return this;
    }

    @Override
    public WMysqlSelectSql count(){
        fields.clear();
        fields.add("count(1) as count");
        return this;
    }

    @Override
    public WMysqlSelectSql limit(int limit, int offset){
        isLimit = true;
        this.limit_x = limit;
        this.limit_y = offset;
        return this;
    }

    @Override
    public WMysqlSelectSql page(int pageNum, int pageSize){
        isLimit = true;
        this.limit_x = pageSize;
        this.limit_y = pageNum*pageSize;
        return this;
    }

    @Override
    public WMysqlSelectSql where(){
        if (this.condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }


    @Override
    public WMysqlSelectSql where(String condition){

        if (this.condition.length() == 0){
            this.condition.append(condition);
        }
        return this;
    }

    @Override
    public WMysqlSelectSql and(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"=? ");
        }
        return this;
    }

    @Override
    public WMysqlSelectSql greater(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"> ? ");
        }
        return this;
    }

    @Override
    public WMysqlSelectSql less(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"<? ");
        }
        return this;
    }

    @Override
    public WMysqlSelectSql greaterAndequals(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+">=? ");
        }
        return this;
    }

    @Override
    public WMysqlSelectSql lessAndequals(String andStr){

        if (condition.length() != 0){
            this.condition.append("and "+andStr+"<=? ");
        }
        return this;
    }

    @Override
    public WMysqlSelectSql or(String orStr){
        if (condition.length() != 0){
            this.condition.append("or "+orStr+"=? ");
        }
        return this;
    }

    @Override
    public WMysqlSelectSql like(String likeStr){
        if (condition.length() != 0){
            this.condition.append("and "+likeStr+"  like ?");
        }
        return this;
    }


    @Override
    public WMysqlSelectSql orderBy(String field, Boolean desc){

        this.orderByField = field;
        this.orderByDesc = desc;

        return this;
    }

    @Override
    public WMysqlSelectSql orderBy(String field){

        this.orderByField = field;
        this.orderByDesc = true;

        return this;
    }


    @Override
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

        if (orderByField!=null){
            sql.append(" ORDER BY "+ orderByField);
            if (orderByDesc == true){
                sql.append(" DESC");
            }
        }

        if (isLimit){
            sql.append(" LIMIT "+limit_x + "  OFFSET "+limit_y);
        }


        log.info("sql: "+sql);
        log.info("values: "+ JSON.toJSONString(values));

        ArrayList<HashMap<String, Object>> data = MySqlDBUtils.find(sql.toString(), values);
        return data;
    }

    @Override
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

        if (orderByField!=null){
            sql.append(" ORDER BY "+ orderByField);
            if (orderByDesc == true){
                sql.append(" DESC");
            }
        }

        if (isLimit){
            sql.append(" LIMIT "+limit_x + "  OFFSET "+limit_y);
        }

        return sql.toString();
    }
}