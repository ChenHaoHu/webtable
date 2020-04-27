package top.hcy.webtable.wsql.structured.impl.mongo;


import org.bson.Document;
import top.hcy.webtable.wsql.structured.WInsertSql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WMongoInsertSql implements WInsertSql {

    private String collection = "";
    private List<Document> documents = new ArrayList<>();
    private ArrayList<String> fields = new ArrayList<>();

    @Override
    public WInsertSql table(String tableName) {
        this.collection = tableName;
        return this;
    }

    @Override
    public WInsertSql fields(String... fields) {
        Collections.addAll(this.fields,fields);
        return this;
    }

    @Override
    public WInsertSql values(String... values) {
        int size = values.length;
        Document document = new Document();
        for (int i = 0; i < size; i++) {
            document.append(fields.get(i),values[i]);
        }
        documents.add(document);
        return this;
    }

    @Override
    public int executeInsert() {
        MongoDBUtils.insert(collection,documents);
        return 0;
    }

    @Override
    public String getSql() {
        return null;
    }
}