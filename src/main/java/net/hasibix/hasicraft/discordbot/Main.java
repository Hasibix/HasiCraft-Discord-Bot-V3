package net.hasibix.hasicraft.discordbot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.github.cdimascio.dotenv.Dotenv;
import net.hasibix.hasicraft.discordbot.models.client.builders.HasiBot;
import net.hasibix.hasicraft.discordbot.models.client.utils.Config;
import net.hasibix.hasicraft.discordbot.models.client.utils.Logger;
import net.hasibix.hasicraft.discordbot.models.client.utils.Config.ConfigObject;

public class Main {
    public static Logger logger;
    public static HasiBot client;
    public static void main(String[] args) {
        Path configPath = Paths.get("config.yml");
        if (!Files.exists(configPath)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.err.println("\033[0;36m" + dtf.format(now) + " | " + "\033[1;31m" + "FATAL: " +  "\033[0;37m" + "Config file not found! Creating one..");
            try {
                File file = new File("config.yml");
                if (file.createNewFile()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write("prefix: \'!\'\nemoji:\n success: \':white_check_mark:\'\n error: \':x:\'\n music: \':musical_note:\'\n warning: \':warning:\'\n musicNote: \':notes:\'\nclient_id: \'00000000000000000\'\nspotify_client_id: \'xxxx000x000x0xx00x000xx0x000x000\'\nminecraft_server_ip: \'0.0.0.0\'\nminecraft_server_port: 5000");
                    writer.close();
                    System.exit(1);
                }
            } catch (IOException e) {
                System.err.println("\033[0;36m" + dtf.format(now) + " | " + "\033[1;31m" + "FATAL: " +  "\033[0;37m" + "Unable to create a config file. Please create one manually!");
            }
        }
        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("BOT_TOKEN");
        Config configs = new Config();
        configs.loadConfig("config.yml");
        ConfigObject config = configs.getConfig();
        logger = new Logger((String) config.get("logs"));

        HasiBot.Intent[] intents = new HasiBot.Intent[] { HasiBot.Intent.ALL_INTENTS };
        
        client = new HasiBot(intents, logger, config);

        client.Login(token);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.Logoff();
        }));
    
    }
}
