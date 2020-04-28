package top.hcy.webtable.wsql.structured.impl.mongo;


import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import static com.mongodb.client.model.Filters.*;

public class MongoTest {


    @Test
    void testSelect() {
        MongoClient mongoClient = new MongoClient("139.155.9.88", 8080);
        MongoDatabase dataSource = mongoClient.getDatabase("mydb");
        MongoCollection<Document> test = dataSource.getCollection("test");
        FindIterable<Document> documents = test.find();

        
        documents.projection(
              new Document("aa",1)  .append("_id", 1)
        );


        MongoCursor<Document> iterator = documents.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }
}