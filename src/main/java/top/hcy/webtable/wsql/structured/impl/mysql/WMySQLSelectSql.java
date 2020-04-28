package top.hcy.webtable.wsql.structured.impl.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.hcy.webtable.common.constant.WConstants;
import top.hcy.webtable.wsql.structured.WSelectSql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


@Slf4j
public class WMySQLSelectSql implements WSelectSql {

    private String table = "";

    private ArrayList<String> fields = new ArrayList<>();

    private int limit_x = 0;

    private int limit_y = 0;

    private boolean isLimit = false;

    private StringBuffer condition  = new StringBuffer();

    private ArrayList<String> conditionValues = new ArrayList<>();

    private String orderByField = null;

    private boolean orderByDesc = false;

    public WMySQLSelectSql() {
    }

    public WMySQLSelectSql(String table) {
        this.table = table;
    }

    @Override
    public WMySQLSelectSql table(String tableName){
        this.table = tableName;
        return this;
    }

    @Override
    public WMySQLSelectSql fields(String... fields){
        Collections.addAll(this.fields,fields);
        return this;
    }

    @Override
    public WMySQLSelectSql fieldsPk(String pk){
        this.fields(pk+" as "+ WConstants.PREFIX_PK+pk);
        return this;
    }

    @Override
    public WMySQLSelectSql count(){
        fields.clear();
        fields.add("count(1) as count");
        return this;
    }

    @Override
    public WMySQLSelectSql limit(int limit, int offset){
        isLimit = true;
        this.limit_x = limit;
        this.limit_y = offset;
        return this;
    }

    @Override
    public WMySQLSelectSql page(int pageNum, int pageSize){
        isLimit = true;
        this.limit_x = pageSize;
        this.limit_y = pageNum*pageSize;
        return this;
    }

    @Override
    public WMySQLSelectSql where(){
        if (this.condition.length() == 0){
            this.condition.append(" 1=1 ");
        }
        return this;
    }



    @Override
    public WMySQLSelectSql and(String andField,String andValue){

        if (condition.length() != 0){
            this.condition.append("and "+andField+"=? ");
            this.conditionValues.add(andValue);
        }
        return this;
    }

    @Override
    public WMySQLSelectSql greater(String gteField,String gteValue){

        if (condition.length() != 0){
            this.condition.append("and "+gteField+"> ? ");
            this.conditionValues.add(gteValue);
        }
        return this;
    }

    @Override
    public WMySQLSelectSql less(String lessField,String lessValue){

        if (condition.length() != 0){
            this.condition.append("and "+lessField+"<? ");
            this.conditionValues.add(lessValue);
        }
        return this;
    }

    @Override
    public WMySQLSelectSql greaterAndequals(String gteAndEqualsField,String gteAndEqualsValue){

        if (condition.length() != 0){
            this.condition.append("and "+gteAndEqualsField+">=? ");
            this.conditionValues.add(gteAndEqualsValue);
        }
        return this;
    }

    @Override
    public WMySQLSelectSql lessAndequals(String lessAndEqualsField,String lessAndEqualsValue){

        if (condition.length() != 0){
            this.condition.append("and "+lessAndEqualsField+"<=? ");
            this.conditionValues.add(lessAndEqualsValue);
        }
        return this;
    }

    @Override
    public WMySQLSelectSql or(String orField,String orValue){
        if (condition.length() != 0){
            this.condition.append("or "+orField+"=? ");
            this.conditionValues.add(orValue);
        }
        return this;
    }

    @Override
    public WMySQLSelectSql like(String likeField,String likeValue){
        if (condition.length() != 0){
            this.condition.append("and "+likeField+"  like ?");
            this.conditionValues.add(likeValue);
        }
        return this;
    }


    @Override
    public WMySQLSelectSql orderBy(String orderByField, Boolean desc){

        this.orderByField = orderByField;
        this.orderByDesc = desc;

        return this;
    }

    @Override
    public WMySQLSelectSql orderBy(String orderByField){

        this.orderByField = orderByField;
        this.orderByDesc = true;

        return this;
    }


    @Override
    public ArrayList<HashMap<String,Object>> executeQuery(){

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
        log.info("values: "+ JSON.toJSONString(conditionValues));

        ArrayList<HashMap<String, Object>> data = MySQLDBUtils.find(sql.toString(), conditionValues);
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