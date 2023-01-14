package net.hasibix.hasicraft.discordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException {
        
        JDA bot = JDABuilder.createDefault(System.getenv("BOT_TOKEN"))
                .setActivity(Activity.listening("hasi help | Play.HasiCraft.net"))
                .build();

    }
}