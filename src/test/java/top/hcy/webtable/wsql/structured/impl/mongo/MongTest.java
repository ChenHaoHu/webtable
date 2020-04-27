package top.hcy.webtable.wsql.structured.impl.mongo;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Test;

public class MongTest {



    @Test
    void executeDelete() {
        //MongoClient mongoClient = MongoClients.create("mongodb://139.155.9.88:8080");
        MongoClient   mongoClient = new MongoClient("139.155.9.88", 8080);
        MongoDatabase database = mongoClient.getDatabase("mydb");
        database.createCollection("test");


    }
}