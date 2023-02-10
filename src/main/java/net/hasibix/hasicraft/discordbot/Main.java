package net.hasibix.hasicraft.discordbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.utils.Config;
import net.hasibix.hasicraft.discordbot.models.client.utils.Logger;
import net.hasibix.hasicraft.discordbot.models.mongodb.MongoDBClient;

public class Main {
    public static Logger logger;
    public static HasiBot client;
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("BOT_TOKEN");
        Config config = new Config();
        config.load();
        logger = new Logger("logs/");

        HasiBot.Intent[] intents = new HasiBot.Intent[] { HasiBot.Intent.ALL_INTENTS };

        client = new HasiBot(intents, logger, config);

        MongoDBClient mongo = new MongoDBClient();
        mongo.Initialize(dotenv.get("MONGO_URI"));

        client.Login(token);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.Logoff();
        }));
    
    }
}
