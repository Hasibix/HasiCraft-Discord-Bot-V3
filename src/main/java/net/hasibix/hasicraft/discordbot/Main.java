package net.hasibix.hasicraft.discordbot;

import javax.security.auth.login.LoginException;

import io.github.cdimascio.dotenv.Dotenv;
import net.hasibix.hasicraft.discordbot.models.client.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.Logger;

public class Main {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("BOT_TOKEN");
        Logger logger = new Logger("logs/");

        HasiBot.Intent[] intents = new HasiBot.Intent[] { HasiBot.Intent.ALL_INTENTS };
        
        HasiBot client = new HasiBot(intents, logger);

        try {
            client.Login(token);
        } catch (LoginException e) {
            logger.FatalError("LoginExecepton occured! " + e);
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.Logoff();
        }));
    

        client.SetActivity("hasi help | Play.HasiCraft.net", HasiBot.ActivityType.Listening, null);
        
    }
}