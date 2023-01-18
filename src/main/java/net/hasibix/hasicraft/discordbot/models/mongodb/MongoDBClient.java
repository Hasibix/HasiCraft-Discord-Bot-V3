package net.hasibix.hasicraft.discordbot.models.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBClient {
    MongoClient mongoClient;

    public MongoClient Initialize(String uri) {
        MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(uri))
                        .retryWrites(true)
                        .writeConcern(WriteConcern.MAJORITY)
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);

        this.mongoClient = mongoClient;

        return mongoClient;
    }
}