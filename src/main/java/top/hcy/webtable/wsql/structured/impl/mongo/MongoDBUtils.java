package top.hcy.webtable.wsql.structured.impl.mongo;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import top.hcy.webtable.wsql.structured.factory.WDataSource;
import top.hcy.webtable.wsql.structured.factory.WSQLDBType;

import java.util.List;

public class MongoDBUtils {

    public static MongoDatabase getMongoDatabase(){
      //  MongoDatabase dataSource = (MongoDatabase)WDataSource.getDefaulteDataSource(WSQLDBType.MongoDB);

        MongoClient mongoClient = new MongoClient("139.155.9.88", 8080);
        MongoDatabase dataSource = mongoClient.getDatabase("mydb");


        return dataSource;
    }

    public static int insert(String collection, List<Document> documents){
        MongoDatabase mongoDatabase = getMongoDatabase();
        MongoCollection<Document> c = mongoDatabase.getCollection(collection);
        c.insertMany(documents);

        return 0;
    }


}