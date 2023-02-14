package net.hasibix.hasicraft.discordbot.models.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import net.hasibix.hasicraft.discordbot.utils.Logger;

public class MongoDBClient {
    MongoClient mongoClient;
    private Logger LOGGER;

    public void Initialize(String uri) {
        MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(uri))
                        .retryWrites(true)
                        .writeConcern(WriteConcern.MAJORITY)
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);

        this.mongoClient = mongoClient;
        this.LOGGER = Logger.of(MongoDBClient.class);

        this.LOGGER.info("Successfully connected to server!");
    }

}