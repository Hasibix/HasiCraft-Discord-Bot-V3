package net.hasibix.hasicraft.discordbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.utils.Config;
import net.hasibix.hasicraft.discordbot.models.client.utils.Logger;
import net.hasibix.hasicraft.discordbot.models.client.utils.Config.ConfigObject;

public class Main {
    public static Logger logger;
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("BOT_TOKEN");
        Config configs = new Config();
        configs.loadConfig("config.yml");
        ConfigObject config = configs.getConfig();
        logger = new Logger((String) config.get("logs"));

        HasiBot.Intent[] intents = new HasiBot.Intent[] { HasiBot.Intent.ALL_INTENTS };
        
        HasiBot client = new HasiBot(intents, logger, config);

        client.Login(token);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.Logoff();
        }));
    

//        client.SetActivity("hasi help | Play.HasiCraft.net", HasiBot.ActivityType.Streaming, "https://twitch.tv/hasibixlive");
        
    }
}
