package net.hasibix.hasicraft.discordbot;

import java.time.LocalDateTime;
import io.github.cdimascio.dotenv.Dotenv;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.mongodb.MongoDBClient;
import net.hasibix.hasicraft.discordbot.utils.Config;
import net.hasibix.hasicraft.discordbot.utils.Logger;

public class Main {
    public static HasiBot client;
    public static LocalDateTime startTime;
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("BOT_TOKEN");
        Config.load();
        Logger.setPath("logs/");

        HasiBot.Intent[] intents = new HasiBot.Intent[] { HasiBot.Intent.ALL_INTENTS };

        client = new HasiBot(intents);

        MongoDBClient mongo = new MongoDBClient();
        mongo.Initialize(dotenv.get("MONGO_URI"));

        client.Login(token);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.Logoff();
        }));
        
        startTime = LocalDateTime.now();
    }
}
