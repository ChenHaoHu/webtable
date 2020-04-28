package top.hcy.webtable.wsql.structured.impl.mongo;


import com.mongodb.client.model.Filters;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;

import org.bson.conversions.Bson;
import top.hcy.webtable.wsql.structured.WSelectSql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Filter;

public class WMongoSelectSql implements WSelectSql {

    private String collection = "";
    private final String pk = "_id";
    private List<Document> documents = new ArrayList<>();
    private ArrayList<String> fields = new ArrayList<>();
    private boolean count = false;
    private boolean where = false;
    private int limit_x = 0;
    private int limit_y = 0;
    private boolean isLimit = false;
    private Document condition;


    @Override
    public WSelectSql table(String tableName) {
        this.collection = tableName;
        return null;
    }

    @Override
    public WSelectSql fields(String... fields) {
        Collections.addAll(this.fields,fields);
        return this;
    }

    @Override
    public WSelectSql fieldsPk(String pk) {
        fields.add(pk);
        return this;
    }

    @Override
    public WSelectSql count() {
        this.count = true;
        return this;
    }

    @Override
    public WSelectSql limit(int limit, int offset) {
        isLimit = true;
        this.limit_x = limit;
        this.limit_y = offset;
        return this;
    }

    @Override
    public WSelectSql page(int pageNum, int pageSize) {
        isLimit = true;
        this.limit_x = pageSize;
        this.limit_y = pageNum*pageSize;
        return this;
    }

    @Override
    public WSelectSql where() {
        this.where = true;
        return this;
    }

    @Override
    public WSelectSql and(String andField, String andValue) {
        Bson and = Filters.and(eq(andField, andValue));

        return null;
    }

    @Override
    public WSelectSql greater(String gteField, String gteValue) {
        return null;
    }

    @Override
    public WSelectSql less(String lessField, String lessValue) {
        return null;
    }

    @Override
    public WSelectSql greaterAndequals(String gteAndEqualsField, String gteAndEqualsValue) {
        return null;
    }

    @Override
    public WSelectSql lessAndequals(String lessAndEqualsField, String lessAndEqualsValue) {
        return null;
    }

    @Override
    public WSelectSql or(String orField, String orValue) {
        return null;
    }

    @Override
    public WSelectSql like(String likeField, String likeValue) {
        return null;
    }


    @Override
    public WSelectSql orderBy(String field, Boolean desc) {
        return null;
    }

    @Override
    public WSelectSql orderBy(String field) {
        return null;
    }

    @Override
    public ArrayList<HashMap<String, Object>> executeQuery() {
        return null;
    }


    @Override
    public String getSql() {
        return null;
    }
}