package net.hasibix.hasicraft.discordbot.models.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;

public class MongoDBClient {
    MongoClient mongoClient;

    public void Initialize(String uri) {
        MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(uri))
                        .retryWrites(true)
                        .writeConcern(WriteConcern.MAJORITY)
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);

        this.mongoClient = mongoClient;

        HasiBot.logger.info("[MongoDB]: Successfully connected to server!");
    }

}