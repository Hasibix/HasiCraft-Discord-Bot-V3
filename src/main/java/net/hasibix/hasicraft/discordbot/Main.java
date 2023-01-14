package net.hasibix.hasicraft.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {
        Dotenv dotenv = Dotenv.configure().load();
        JDA bot = JDABuilder.createDefault(dotenv.get("BOT_TOKEN"))
                .setActivity(Activity.listening("hasi help | Play.HasiCraft.net"))
                .build();

    }
}